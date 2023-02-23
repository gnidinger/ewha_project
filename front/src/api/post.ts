import { PostData } from "../pages/Write";
import axiosApi from "./axiosApi";

export const getLatest = async( page: number ) => {
  try {
    const { data } = await axiosApi.get(`/feeds/newest?page=${page}`);
    return data;
  }
  catch(e) {
    console.log(e);
  }
};

export const getPostsBySubject = async( subject: string, page: number ) => {
  try {
    const { data } = await axiosApi.get(`/feeds/categories?category=${subject}&page=${page}`);
    return data;
  }
  catch(e) {
    console.log(e);
  }
};

export const getPost = async( postId: string ) => {
  try {
    const { data } = await axiosApi.get(`/feeds/${postId}`);
    return data;
  }
  catch(e) {
    console.log(e);
  }
};

export const writePost = async( post: PostData, image?: File, postId?: string ) => {
  try {
    const body = new FormData();
    const blob = new Blob([JSON.stringify(post)], {
      type: 'application/json'
    })
    body.append('post', blob);
    if(image instanceof File) body.append('image', image);
    let data;
    if(postId) data = await axiosApi.patch(`/feeds/${postId}/edit`);
    else data = await axiosApi.post('/feeds/add', body);
    return data;
  }
  catch(e) {
    console.log(e);
  }
};

export const like = async( postId: string ) => {
  try {
    const { data } = await axiosApi.patch(`/feeds/${postId}/like`);
    return data;
  }
  catch(e) {
    console.log(e);
  }
};

export const dislike =async( postId: string ) => {
  try {
    const { data } = await axiosApi.patch(`/feeds/${postId}/dislike`);
    return data;
  }
  catch(e) {
    console.log(e);
  }
};