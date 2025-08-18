import { shallowMount } from '@vue/test-utils';
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';

import type { VueWrapper } from '@vue/test-utils';

import StoragePage from '@/pages/StoragePage.vue';
import { getAllTasks, createTask } from '@/api/tasks';

vi.mock('@/api/tasks', () => ({
  createTask: vi.fn(),
  getAllTasks: vi.fn(),
}));

const mockI18n = {
  $t: vi.fn((key) => key),
};

describe('StoragePage', () => {
  let wrapper: VueWrapper<InstanceType<typeof StoragePage>>;

  const mockTasks = [
    { id: '1', name: 'Task 1', namespace: 'Namespace 1', version: '1.0.0', date: '18/08/2025' },
    { id: '2', name: 'Task 2', namespace: 'Namespace 2', version: '1.0.0', date: '18/08/2025' },
  ];

  const mockCreateTask = vi.mocked(createTask);
  const mockGetAllTasks = vi.mocked(getAllTasks).mockResolvedValue([...mockTasks]);

  beforeEach(() => {
    wrapper = shallowMount(StoragePage, {
      global: {
        mocks: mockI18n,
        stubs: {
          'b-upload': {
            template: '<div><slot /></div>',
            props: ['modelValue'],
            emits: ['update:modelValue'],
          },
          'b-input': {
            template: '<input />',
            props: ['modelValue', 'icon', 'placeholder'],
          },
          'AppCard': {
            template: '<div class="app-card"></div>',
            props: ['app'],
          },
        },
      },
    });
  });

  afterEach(() => {
    wrapper.unmount();
    vi.clearAllMocks();
  });

  describe('Initialisation', () => {
    it('should display translated strings correctly', () => {
      expect(mockI18n.$t).toHaveBeenCalledWith('storage');
      expect(mockI18n.$t).toHaveBeenCalledWith('upload');
      expect(mockI18n.$t).toHaveBeenCalledWith('search');
    });

    it('should call getAllTasks on mount', async () => {
      expect(mockGetAllTasks).toHaveBeenCalledOnce();
    });

    it('should populate tasks with data from getAllTasks', async () => {
      expect(wrapper.vm.tasks).toEqual(mockTasks);
    });
  });

  describe('File Upload Handling', () => {
    it('should not call createTask when no file is selected', async () => {
      wrapper.vm.selectedFile = null;
      await wrapper.vm.handleFileChange();

      expect(mockCreateTask).not.toHaveBeenCalled();
    });

    it('should call createTask with FormData when file is selected', async () => {
      const mockFile = new File(['content'], 'test.txt');
      const mockCreatedTask = { id: '3', name: 'New Task' };
      mockCreateTask.mockResolvedValue(mockCreatedTask);

      wrapper.vm.selectedFile = mockFile;
      await wrapper.vm.handleFileChange();

      expect(mockCreateTask).toHaveBeenCalledOnce();
      const formDataArg = mockCreateTask.mock.calls[0][0];
      expect(formDataArg).toBeInstanceOf(FormData);
      expect(formDataArg.get('task')).toBe(mockFile);
    });

    it('should add created task to tasks array when successful upload', async () => {
      const mockFile = new File(['content'], 'test.txt');
      const mockCreatedTask = { id: '4', name: 'New Task 4' };
      mockCreateTask.mockResolvedValue(mockCreatedTask);

      wrapper.vm.tasks = [...mockTasks];
      wrapper.vm.selectedFile = mockFile;
      await wrapper.vm.handleFileChange();

      expect(wrapper.vm.tasks).toHaveLength(3);
      expect(wrapper.vm.tasks[2]).toEqual(mockCreatedTask);
    });

    it('should reset selectedFile when successful upload', async () => {
      const mockFile = new File(['content'], 'test.txt');
      mockCreateTask.mockResolvedValue({ id: '5', name: 'New Task 5' });

      wrapper.vm.selectedFile = mockFile;
      await wrapper.vm.handleFileChange();

      expect(wrapper.vm.selectedFile).toBe(null);
    });

    it('should handle upload error gracefully', async () => {
      const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => { });
      const mockFile = new File(['content'], 'test.txt');
      mockCreateTask.mockRejectedValue(new Error('Upload failed'));

      wrapper.vm.selectedFile = mockFile;
      await wrapper.vm.handleFileChange();

      expect(consoleErrorSpy).toHaveBeenCalledWith('Upload error:', expect.any(Error));
      expect(wrapper.vm.selectedFile).toBe(null);
      consoleErrorSpy.mockRestore();
    });

    it('should reset selectedFile even when upload fails', async () => {
      const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => { });
      const mockFile = new File(['content'], 'test.txt');
      mockCreateTask.mockRejectedValue(new Error('Upload failed'));

      wrapper.vm.selectedFile = mockFile;
      await wrapper.vm.handleFileChange();

      expect(wrapper.vm.selectedFile).toBe(null);
      consoleErrorSpy.mockRestore();
    });
  });
});
