import { shallowMount } from '@vue/test-utils';
import { beforeEach, describe, it, expect, vi } from 'vitest';
import type { VueWrapper } from '@vue/test-utils';

import AppCard from '@/components/app/AppCard.vue';

const downloadTaskMock = vi.fn();
vi.mock('@/composables/useTask', () => ({
  useTask: () => ({
    downloadTask: downloadTaskMock,
  }),
}));

vi.mock('vue-i18n', () => ({
  useI18n: () => ({
    t: (key: string) => key,
  }),
}));

describe('AppCard.vue', () => {
  let wrapper: VueWrapper<InstanceType<typeof AppCard>>;

  const mockTask = {
    id: '42',
    name: 'Test App',
    date: '2025-08-20',
    version: '1.2.3',
    description: 'This is a test app',
    namespace: 'com.test.app',
    authors: [],
  };

  beforeEach(() => {
    wrapper = shallowMount(AppCard, {
      props: {
        app: mockTask,
      },
      global: {
        stubs: {
          'b-button': true,
          'router-link': true,
        },
      },
    });
  });

  it('should render app details correctly', () => {
    expect(wrapper.text()).toContain(mockTask.name);
    expect(wrapper.text()).toContain(mockTask.date);
    expect(wrapper.text()).toContain(mockTask.version);
    expect(wrapper.text()).toContain(mockTask.description);
  });

  it('should call downloadTask when clicking download', async () => {
    const downloadBtn = wrapper.find('.card-footer-item');
    await downloadBtn.trigger('click');

    expect(downloadTaskMock).toHaveBeenCalledWith(mockTask.namespace, mockTask.version);
  });
});
