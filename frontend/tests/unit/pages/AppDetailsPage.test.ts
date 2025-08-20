import { createTestingPinia } from '@pinia/testing';
import { shallowMount } from '@vue/test-utils';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { useRoute } from 'vue-router';
import type { VueWrapper } from '@vue/test-utils';

import AppDetailsPage from '@/pages/AppDetailsPage.vue';
import { useTaskStore } from '@/stores/taskStore';

vi.mock('vue-router', () => ({
  useRoute: vi.fn(),
}));

describe('AppDetailsPage.vue', () => {
  let wrapper: VueWrapper<InstanceType<typeof AppDetailsPage>>;

  const mockTask = {
    id: '42',
    name: 'Test App',
    namespace: 'test-namespace',
    version: '1.0.0',
    date: '20/08/2025',
  };

  vi.mocked(useRoute).mockReturnValue({
    params: {
      namespace: mockTask.namespace,
      version: mockTask.version,
    },
    matched: [],
    name: 'AppDetails',
    fullPath: `/apps/${mockTask.namespace}/${mockTask.version}`,
    query: {},
    hash: '',
    path: `/apps/${mockTask.namespace}/${mockTask.version}`,
    redirectedFrom: undefined,
    meta: {},
  });

  const pinia = createTestingPinia({
    createSpy: vi.fn,
    stubActions: false,
  });

  const taskStore = useTaskStore();
  taskStore.fetchTask = vi.fn().mockResolvedValue(mockTask);

  beforeEach(() => {
    wrapper = shallowMount(AppDetailsPage, {
      global: {
        plugins: [
          pinia,
        ],
        stubs: {
          'b-loading': true,
        },
      },
    });
  });

  it('should fetch the task from the task store', async () => {
    expect(taskStore.fetchTask).toHaveBeenCalledWith(mockTask.namespace, mockTask.version);

    expect(wrapper.text()).toContain('false');
    expect(wrapper.text()).toContain(mockTask.name);
  });
});
