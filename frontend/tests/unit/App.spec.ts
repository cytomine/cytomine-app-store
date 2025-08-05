import { mount } from '@vue/test-utils';
import { describe, it, expect } from 'vitest';
import Buefy from 'buefy';

import App from '@/App.vue';

describe('App', () => {
  it('mounts renders properly', () => {
    const wrapper = mount(App, {
      global: {
        plugins: [
          Buefy,
        ],
        mocks: {
          $t: (message: string) => message,
        },
      },
    });
    expect(wrapper.text()).toContain('hello-world');
  });
});
