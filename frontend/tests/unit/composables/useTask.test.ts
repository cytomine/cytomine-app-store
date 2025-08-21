import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { nextTick } from 'vue';
import { mount } from '@vue/test-utils';

import { useTask } from '@/composables/useTask';
import { getAllTasks, searchTasks } from '@/api/tasks';
import type { App, Search } from '@/types/types';

vi.mock('@/api/tasks', () => ({
  getAllTasks: vi.fn(),
  searchTasks: vi.fn(),
}));

const mockGetAllTasks = vi.mocked(getAllTasks);
const mockSearchTasks = vi.mocked(searchTasks);

const mockTasks: App[] = [
  { id: '1', name: 'task1', namespace: 'ns1', version: '1.0', date: '20/08/2025', authors: [] },
  { id: '2', name: 'task2', namespace: 'ns2', version: '2.0', date: '20/08/2025', authors: [] },
  { id: '3', name: 'task3', namespace: 'ns1', version: '1.5', date: '20/08/2025', authors: [] },
];

const mockSearchResults: Search[] = [
  { name: 'task1', namespace: 'ns1', nameshort: 'n1', imageName: 'i1', version: '1.0' },
  { name: 'task3', namespace: 'ns1', nameshort: 'n2', imageName: 'i2', version: '1.5' },
];

const TestComponent = {
  setup() {
    return useTask();
  },
  template: '<div></div>',
};

describe('useTask', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockGetAllTasks.mockResolvedValue(mockTasks);
    mockSearchTasks.mockResolvedValue(mockSearchResults);
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  describe('setup', () => {
    it('should initialise with default values', () => {
      const wrapper = mount(TestComponent);
      const composable = wrapper.vm;

      expect(composable.searchString).toBe('');
      expect(composable.result).toEqual([]);
      expect(composable.tasks).toEqual([]);
      expect(composable.isLoading).toBe(true);
    });

    it('should load all tasks on mount', async () => {
      const wrapper = mount(TestComponent);

      await nextTick();

      expect(mockGetAllTasks).toHaveBeenCalledOnce();
      expect(wrapper.vm.tasks).toEqual(mockTasks);
      expect(wrapper.vm.isLoading).toBe(false);
    });
  });

  describe('search', () => {
    it('should search when searchString changes', async () => {
      const wrapper = mount(TestComponent);
      await nextTick();

      vi.clearAllMocks();
      mockGetAllTasks.mockResolvedValue(mockTasks);

      (wrapper.vm.searchString as string) = 'test query';
      await nextTick();

      expect(mockSearchTasks).toHaveBeenCalledWith('test query');
      await nextTick();

      expect(wrapper.vm.result).toEqual(mockSearchResults);
    });

    it('should filter tasks based on search results', async () => {
      const wrapper = mount(TestComponent);

      (wrapper.vm.searchString as string) = 'test';
      await nextTick();

      const expectedFilteredTasks = mockTasks.filter(task =>
        mockSearchResults.some(result =>
          result.name === task.name &&
          result.namespace === task.namespace &&
          result.version === task.version,
        ),
      );

      expect(wrapper.vm.tasks).toEqual(expectedFilteredTasks);
    });

    it('should return all tasks when search string is empty', async () => {
      const wrapper = mount(TestComponent);

      (wrapper.vm.searchString as string) = '';
      await nextTick();

      expect(mockGetAllTasks).toHaveBeenCalledOnce();
      expect(mockSearchTasks).not.toHaveBeenCalled();
      await nextTick();

      expect(wrapper.vm.result).toEqual([]);
      expect(wrapper.vm.tasks).toEqual(mockTasks);
    });

    it('should return all tasks when search string is whitespace only', async () => {
      const wrapper = mount(TestComponent);

      await nextTick();

      vi.clearAllMocks();
      mockGetAllTasks.mockResolvedValue(mockTasks);

      (wrapper.vm.searchString as string) = '   ';
      await nextTick();

      expect(mockGetAllTasks).toHaveBeenCalledOnce();
      expect(mockSearchTasks).not.toHaveBeenCalled();
    });

    it('should return no tasks when no search results match', async () => {
      const wrapper = mount(TestComponent);

      await nextTick();

      mockSearchTasks.mockResolvedValue([]);

      (wrapper.vm.searchString as string) = 'nonexistent';
      await nextTick();

      expect(wrapper.vm.tasks).toEqual([]);
      expect(wrapper.vm.result).toEqual([]);
    });
  });
});
