import { useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import { ErrorHandler, Notification } from 'types';
import HttpError from 'utils/HttpError';
import { resolveHttpErrorResponse } from 'utils/error';

interface CustomQueryOption extends UseQueryOptions<Notification[], HttpError> {
  errorHandler?: ErrorHandler;
}

const getNotifications = async (errorHandler?: ErrorHandler) => {
  try {
    const { data } = await api.get('/members/me/notifications');

    return data;
  } catch (error) {
    resolveHttpErrorResponse({
      errorResponse: error.response,
      defaultErrorMessage: '알림 목록을 불러오는 과정에서 에러가 발생했습니다',
      errorHandler,
    });
  }
};

const useNotiLoad = ({ errorHandler, ...option }: CustomQueryOption) => {
  return useQuery<Notification[]>('notification', () => getNotifications(errorHandler), option);
};

export default useNotiLoad;
