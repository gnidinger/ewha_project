import axiosApi from "./axiosApi";

export const writeComment = async( feedId: string, body: string ) => {
  try {
    const { data } = await axiosApi.post(`/feeds/${feedId}/comments/add`, { body });
    return data;
  }
  catch(e) {
    console.log(e);
  }
};