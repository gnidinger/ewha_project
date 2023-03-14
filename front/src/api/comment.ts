import axiosApi from './axiosApi';

export const writeComment = async( feedId: string, body: string ) => {
  try {
    const { data } = await axiosApi.post(`/feeds/${feedId}/comments/add`, { body });
    return data;
  }
  catch(e) {
    console.log(e);
  }
};

export const modify = async( commentId: string, body: string ) => {
  try {
    const { data } = await axiosApi.patch(`/comments/${commentId}/edit`, { body });
    return data;
  }
  catch(e) {
    console.log(e);
  }
};

export const deleteComment = async( commentId: string ) => {
  try {
    const { data } = await axiosApi.delete(`/comments/${commentId}/delete`);
    return data;
  }
  catch(e) {
    console.log(e);
  }
};

export const like = async( feedId: string ) => {
  try {
    const { data } = await axiosApi.patch(`/comments/${feedId}/like`);
    return data;
  }
  catch(e) {
    console.log(e);
  }
};

export const dislike = async( feedId: string ) => {
  try {
    const { data } = await axiosApi.patch(`/comments/${feedId}/dislike`);
    return data;
  }
  catch(e) {
    console.log(e);
  }
};