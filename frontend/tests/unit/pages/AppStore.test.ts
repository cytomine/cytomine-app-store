import { shallowMount } from '@vue/test-utils';
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';

import type { VueWrapper } from '@vue/test-utils';

import AppStore from '@/pages/AppStore.vue';
import { getAllTasks, searchTasks } from '@/api/tasks';

vi.mock('@/api/tasks', () => ({
  getAllTasks: vi.fn(),
  searchTasks: vi.fn(),
}));

const mockI18n = {
  $t: vi.fn((key: string) => key),
};

describe('AppStore', () => {
  let wrapper: VueWrapper<InstanceType<typeof AppStore>>;

  const mockTasks = [
    { id: '1', name: 'Task 1', namespace: 'Namespace 1', version: '1.0.0', date: '18/08/2025' },
    { id: '2', name: 'Task 2', namespace: 'Namespace 2', version: '1.0.0', date: '18/08/2025' },
  ];

  const mockGetAllTasks = vi.mocked(getAllTasks).mockResolvedValue([...mockTasks]);
  const mockSearchTasks = vi.mocked(searchTasks);

  beforeEach(() => {
    wrapper = shallowMount(AppStore, {
      global: {
        mocks: mockI18n,
        stubs: {
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
      expect(mockI18n.$t).toHaveBeenCalledWith('store');
      expect(mockI18n.$t).toHaveBeenCalledWith('search');
    });

    it('should call getAllTasks on mount', async () => {
      expect(mockGetAllTasks).toHaveBeenCalledOnce();
    });

    it('should populate tasks with data from getAllTasks', async () => {
      expect(wrapper.vm.tasks).toEqual(mockTasks);
    });
  });
});
