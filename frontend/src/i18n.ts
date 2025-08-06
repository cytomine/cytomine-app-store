import { createI18n } from 'vue-i18n';

import en from './locales/json/en.i18n.json';
import fr from './locales/json/fr.i18n.json';

type MessageSchema = typeof en & typeof fr;
type Locale = 'en' | 'fr';

const i18n = createI18n<[MessageSchema], Locale>({
  legacy: false,
  locale: 'en',
  fallbackLocale: 'en',
  messages: {
    en,
    fr,
  },
});

export default i18n;