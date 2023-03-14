import axiosApi from './axiosApi';

export const getMessages = async() => {
  try {
    const { data: { data } } = await axiosApi.get('/chats/rooms');
    return data;
  }
  catch(e) {
    console.log(e);
  }
};