import { shallowMount } from '@vue/test-utils';
import { describe, it, expect } from 'vitest';

import App from '@/pages/GlobalDashboard.vue';

describe('App', () => {
  it('mounts renders properly', () => {
    const wrapper = shallowMount(App, {
      global: {
        mocks: {
          $t: (message: string) => message,
        },
      },
    });
    expect(wrapper.text()).toContain('hello-world');
  });
});
