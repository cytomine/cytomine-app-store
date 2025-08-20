import { shallowMount } from '@vue/test-utils';
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import type { VueWrapper } from '@vue/test-utils';

import AppDetailsPage from '@/pages/AppDetailsPage.vue';
import { getTask } from '@/api/tasks';
import type { App } from '@/types/types';

vi.mock('@/api/tasks', () => ({
  getTask: vi.fn(),
}));

vi.mock('vue-i18n', () => ({
  useI18n: () => ({
    t: (msg: string) => msg,
  }),
}));

describe('AppDetailsPage.vue', () => {
  let wrapper: VueWrapper<InstanceType<typeof AppDetailsPage>>;

  const mockTask: App = {
    id: '42',
    name: 'test-app',
    namespace: 'default',
    version: '1.0.0',
    date: '20/08/2025',
  };

  const mockGetTask = vi.mocked(getTask).mockResolvedValue(mockTask);

  beforeEach(() => {
    wrapper = shallowMount(AppDetailsPage, {
      props: { app: mockTask },
      global: {
        stubs: {
          'b-loading': true,
        },
      },
    });
  });

  afterEach(() => {
    wrapper.unmount();
    vi.clearAllMocks();
  });

  describe('Initialisation', () => {
    it('should call getTaskById on mount', () => {
      expect(mockGetTask).toHaveBeenCalledWith(mockTask.namespace, mockTask.version);
    });
  });
});
