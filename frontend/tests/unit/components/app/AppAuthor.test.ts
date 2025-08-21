import { shallowMount } from '@vue/test-utils';
import { describe, it, expect } from 'vitest';

import AppAuthor from '@/components/app/AppAuthor.vue';
import type { Author } from '@/types/types';

describe('AppAuthor.vue', () => {
  const factory = (author: Author) => {
    return shallowMount(AppAuthor, {
      props: {
        author,
      },
    });
  };

  it('should render full name correctly', () => {
    const wrapper = factory({
      firstName: 'John',
      lastName: 'Smith',
      email: 'john@cytomine.org',
      organization: 'Cytomine',
      isContact: true,
    });

    expect(wrapper.text()).toContain('John Smith');
  });

  it('should render email with icon', () => {
    const wrapper = factory({
      firstName: 'Jane',
      lastName: 'Doe',
      email: 'jane@cytomine.com',
      organization: 'Cytomine',
      isContact: true,
    });

    const email = wrapper.find('.fa-envelope').element.nextSibling?.textContent?.trim();
    expect(email).toBe('jane@cytomine.com');
  });

  it('should render organization with icon', () => {
    const wrapper = factory({
      firstName: 'Alice',
      lastName: 'Wang',
      email: 'alice@cytomine.com',
      organization: 'Cytomine',
      isContact: true,
    });

    const org = wrapper.find('.fa-building').element.nextSibling?.textContent?.trim();
    expect(org).toBe('Cytomine');
  });
});
