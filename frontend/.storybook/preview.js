import { configure, addDecorator } from '@storybook/react';
import { MemoryRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';

import GlobalStyle from '../src/Global.styles';
import ModalProvider from '../src/context/modal/ModalProvider';
import AsyncBoundary from '../src/components/AsyncBoundary';

export const parameters = {
  actions: { argTypesRegex: '^on[A-Z].*' },
  controls: {
    matchers: {
      color: /(background|color)$/i,
      date: /Date$/,
    },
  },
};

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      suspense: true,
      useErrorBoundary: true,
    },
  },
});

addDecorator((story) => (
  <>
    <QueryClientProvider client={queryClient}>
      <MemoryRouter initialEntries={['/']}>
        <GlobalStyle />
        <AsyncBoundary rejectedFallback={<div>에러났어용 🚨</div>}>
          <ModalProvider>{story()}</ModalProvider>
        </AsyncBoundary>
      </MemoryRouter>
    </QueryClientProvider>
  </>
));

configure(require.context('../src', true, /\.stories\.js?$/), module);
