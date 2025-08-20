import { shallowMount } from '@vue/test-utils';
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';

import type { VueWrapper } from '@vue/test-utils';

import AppCard from '@/components/app/AppCard.vue';
import AppStore from '@/pages/AppStore.vue';
import { getAllTasks, searchTasks } from '@/api/tasks';

vi.mock('@/api/tasks', () => ({
  getAllTasks: vi.fn(),
  searchTasks: vi.fn(),
}));

vi.mock('vue-i18n', () => ({
  useI18n: () => ({
    t: (msg: string) => msg,
  }),
}));

describe('AppStore.vue', () => {
  let wrapper: VueWrapper<InstanceType<typeof AppStore>>;

  const mockTasks = [
    {
      id: '1',
      name: 'Task 1',
      namespace: 'Namespace 1',
      version: '1.0.0',
      date: '18/08/2025',
    },
    {
      id: '2',
      name: 'test-app',
      namespace: 'default',
      version: '1.0.0',
      date: '19/08/2025',
    },
  ];

  const mockSearchResults = [
    {
      name: 'test-app',
      namespace: 'default',
      nameshort: 'test',
      imageName: 'test-image',
      version: '1.0.0',
    }
  ];

  const mockGetAllTasks = vi.mocked(getAllTasks).mockResolvedValue([...mockTasks]);
  const mockSearchTasks = vi.mocked(searchTasks).mockResolvedValue([...mockSearchResults]);

  beforeEach(() => {
    wrapper = shallowMount(AppStore, {
      global: {
        stubs: {
          'b-input': {
            template: '<input v-bind="$attrs" :value="modelValue" @input="handleInput" />',
            props: ['modelValue', 'icon', 'placeholder'],
            emits: ['update:modelValue'],
            methods: {
              handleInput(event: Event) {
                this.$emit('update:modelValue', (event.target as HTMLInputElement).value);
              }
            }
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

  describe('setup', () => {
    it('should render an AppCard component for each task', () => {
      const cards = wrapper.findAllComponents(AppCard);

      expect(cards).toHaveLength(mockTasks.length);
      cards.forEach((card, index) => {
        expect(card.props('app')).toEqual(mockTasks[index]);
      });
    });
  });

  describe('search', () => {
    it('should update search input model correctly', async () => {
      const inputElement = wrapper.find('input');

      await inputElement.setValue('new search');

      expect(wrapper.vm.searchString).toBe('new search');
    });

    it('should filter tasks when query is provided', async () => {
      expect(wrapper.vm.tasks).toHaveLength(mockTasks.length);

      const inputElement = wrapper.find('input');
      await inputElement.setValue('test');

      expect(mockSearchTasks).toHaveBeenCalledWith('test');
      expect(wrapper.vm.result).toEqual(mockSearchResults);
      expect(wrapper.vm.tasks).toHaveLength(1);
      expect(wrapper.vm.tasks[0].name).toBe('test-app');
    });

    it('should reset to all tasks when query is cleared', async () => {
      expect(wrapper.find('input')).toBeDefined();

      const searchInput = wrapper.find('input');
      await searchInput.setValue('test');
      await searchInput.setValue('');

      expect(mockGetAllTasks).toHaveBeenCalledTimes(2);
      expect(mockSearchTasks).toHaveBeenCalledOnce();
      expect(wrapper.vm.result).toEqual([]);
      expect(wrapper.vm.tasks).toEqual(mockTasks);
    });

    it('should handle search when query is only whitespaces', async () => {
      const inputElement = wrapper.find('input');
      await inputElement.setValue('   ');

      expect(mockSearchTasks).not.toHaveBeenCalled();
      expect(mockGetAllTasks).toHaveBeenCalledTimes(2);
    });

    it('should not search when query is empty', async () => {
      const inputElement = wrapper.find('input');
      await inputElement.setValue('');

      expect(mockSearchTasks).not.toHaveBeenCalled();
    });
  });
});
