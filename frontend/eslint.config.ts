import { globalIgnores } from 'eslint/config';
import { defineConfigWithVueTs, vueTsConfigs } from '@vue/eslint-config-typescript';
import pluginVue from 'eslint-plugin-vue';
import pluginVitest from '@vitest/eslint-plugin';
import pluginPlaywright from 'eslint-plugin-playwright';

// To allow more languages other than `ts` in `.vue` files, uncomment the following lines:
// import { configureVueProject } from '@vue/eslint-config-typescript'
// configureVueProject({ scriptLangs: ['ts', 'tsx'] })
// More info at https://github.com/vuejs/eslint-config-typescript/#advanced-setup

export default defineConfigWithVueTs(
  {
    name: 'app/files-to-lint',
    files: ['**/*.{ts,vue}'],
    rules: {
      'array-bracket-spacing': ['error', 'never'],
      'brace-style': ['error', '1tbs'],
      'camelcase': ['error', { allow: ['$_veeValidate'] }],
      'curly': ['error', 'all'],
      'eqeqeq': ['error', 'smart'],
      'indent': ['error', 2, { 'SwitchCase': 1, 'ignoredNodes': ['TemplateLiteral'] }],
      'keyword-spacing': ['error'],
      'no-console': ['off'],
      'no-redeclare': ['error'],
      'no-undef': ['error'],
      'no-unused-vars': ['error'],
      'no-var': ['error'],
      'object-curly-spacing': ['error', 'always'],
      'quotes': ['error', 'single', { 'avoidEscape': true }],
      'semi': ['error', 'always'],
      'space-before-blocks': ['error', 'always'],
      'space-before-function-paren': ['error', {
        anonymous: 'always',
        named: 'never',
        asyncArrow: 'always',
      }],
      'space-infix-ops': ['error'],
      'space-in-parens': ['error'],
    },
  },

  globalIgnores(['**/dist/**', '**/dist-ssr/**', '**/coverage/**']),

  pluginVue.configs['flat/essential'],
  vueTsConfigs.recommended,

  {
    ...pluginVitest.configs.recommended,
    files: ['src/**/__tests__/*'],
  },

  {
    ...pluginPlaywright.configs['flat/recommended'],
    files: ['e2e/**/*.{test,spec}.{js,ts,jsx,tsx}'],
  },
);
