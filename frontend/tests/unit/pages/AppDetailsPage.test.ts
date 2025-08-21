import { createTestingPinia } from '@pinia/testing';
import { flushPromises, shallowMount } from '@vue/test-utils';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { useRoute } from 'vue-router';
import type { VueWrapper } from '@vue/test-utils';

import AppDetailsPage from '@/pages/AppDetailsPage.vue';
import { useTaskStore } from '@/stores/taskStore';

vi.mock('vue-i18n', () => ({
  useI18n: () => ({
    t: (msg: string) => msg,
  }),
}));

const mockRouter = { back: vi.fn() };
vi.mock('vue-router', () => ({
  useRoute: vi.fn(),
  useRouter: () => mockRouter,
}));

describe('AppDetailsPage.vue', () => {
  let wrapper: VueWrapper<InstanceType<typeof AppDetailsPage>>;

  const mockTask = {
    id: '42',
    name: 'Test App',
    namespace: 'test-namespace',
    version: '1.0.0',
    date: '20/08/2025',
    authors: [
      {
        firstName: 'John',
        lastName: 'Doe',
        organization: 'Cytomine',
        email: 'test@cytomine.org',
        isContact: true,
      },
    ],
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
          'b-button': true,
          'b-loading': true,
        },
      },
    });
  });

  it('should show loader until fetch is completed', async () => {
    const wrapperLoader = shallowMount(AppDetailsPage, {
      global: {
        plugins: [
          pinia,
        ],
        stubs: {
          'b-button': true,
          'b-loading': true,
        },
      },
    });

    expect(wrapperLoader.find('b-loading-stub').exists()).toBe(true);
    expect(taskStore.fetchTask).toHaveBeenCalledWith(mockTask.namespace, mockTask.version);

    await flushPromises();

    expect(wrapperLoader.find('b-loading-stub').exists()).toBe(false);
  });

  it('should fetch the task from the task store on mount', () => {
    expect(taskStore.fetchTask).toHaveBeenCalledWith(mockTask.namespace, mockTask.version);
  });

  it('should render task details', () => {
    expect(wrapper.text()).toContain(mockTask.name);
    expect(wrapper.text()).toContain(mockTask.version);
    expect(wrapper.text()).toContain(mockTask.date);

    mockTask.authors.map(author => {
      expect(wrapper.text()).toContain(author.firstName);
      expect(wrapper.text()).toContain(author.lastName);
    });
  });

  it('should call router.back() when back button is clicked', async () => {
    await wrapper.find('b-button-stub').trigger('click');

    expect(mockRouter.back).toHaveBeenCalledOnce();
  });
});
