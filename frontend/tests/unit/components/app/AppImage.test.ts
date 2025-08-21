import { shallowMount } from '@vue/test-utils';
import { afterEach, beforeEach, describe, it, expect, vi } from 'vitest';
import type { VueWrapper } from '@vue/test-utils';

import AppImage from '@/components/app/AppImage.vue';

describe('AppImage.vue', () => {
  let wrapper: VueWrapper<InstanceType<typeof AppImage>>;

  const createWrapper = (props = {}) => {
    return shallowMount(AppImage, {
      props,
      global: {
        stubs: {
          'b-image': {
            template: '<img v-bind="$attrs" @error="$emit(\'error\', $event)" />',
            inheritAttrs: false,
          },
        },
      },
    });
  };

  beforeEach(() => {
    vi.stubEnv('VITE_API_BASE_URL', 'https://api.example.com');
  });

  afterEach(() => {
    wrapper?.unmount();
    vi.unstubAllEnvs();
  });

  describe('Image URL generation', () => {
    it('should generate correct URL when all props are provided', () => {
      wrapper = createWrapper({
        namespace: 'my-app',
        version: '1.0.0',
      });

      const img = wrapper.find('img');
      expect(img.attributes('src')).toBe('https://api.example.com/api/v1/tasks/my-app/1.0.0/logo');
    });

    it('should return fallback URL when namespace is missing', () => {
      wrapper = createWrapper({
        version: '1.0.0',
      });

      const img = wrapper.find('img');
      expect(img.attributes('src')).toBe(wrapper.vm.fallbackUrl);
    });

    it('should return fallback URL when version is missing', () => {
      wrapper = createWrapper({
        namespace: 'my-app',
      });

      const img = wrapper.find('img');
      expect(img.attributes('src')).toBe(wrapper.vm.fallbackUrl);
    });

    it('should return fallback URL when base URL is not configured', () => {
      vi.stubEnv('VITE_API_BASE_URL', '');

      wrapper = createWrapper({
        namespace: 'my-app',
        version: '1.0.0',
      });

      const img = wrapper.find('img');
      expect(img.attributes('src')).toBe(wrapper.vm.fallbackUrl);
    });

    it('should handle undefined environment variable', () => {
      vi.stubEnv('VITE_API_BASE_URL', undefined);

      wrapper = createWrapper({
        namespace: 'my-app',
        version: '1.0.0',
      });

      const img = wrapper.find('img');
      expect(img.attributes('src')).toBe(wrapper.vm.fallbackUrl);
    });

    it('should handle special characters in namespace and version', () => {
      wrapper = createWrapper({
        namespace: 'my-app@scope',
        version: '1.0.0-beta.1',
      });

      const img = wrapper.find('img');
      expect(img.attributes('src')).toBe('https://api.example.com/api/v1/tasks/my-app@scope/1.0.0-beta.1/logo');
    });
  });
});
