import axiosApi from "./axiosApi";
import { SignupData } from "../components/signup/SignupForm";
import axios from "axios";
import { setCookie } from "./cookie";
import { Info } from "../pages/SetInfo";
import { SettingData } from "../pages/Setting";

export const check = async({ userId, nickname, password }: SignupData) => {
  try {
    const body = { userId, nickname, password };
    const { status } = await axiosApi.post('/users/verification', body);
    return status;
  }
  catch(e) {
    console.log(e);
    if(axios.isAxiosError(e)) {
      if(e.response) {
        return(e.response.data.data);
      }
    }
    else console.log(e);
  }
};

export const getCertificationNumber = async( phone: string ) => {
  try {
    const body = { phoneNumber: phone };
    const data = await axiosApi.post('/sms/send', body);
    return data;
  }
  catch(e) {
    console.log(e);
  }
};

export const verify = async( phone: string, code: string ) => {
  try {
    const body = { phoneNumber: phone, certificationNumber: code };
    const { status } = await axiosApi.post('/sms/verification', body);
    return status;
  }
  catch(e) {
    console.log(e);
  }
};

export const signup = async({ userId, nickname, password, centercode, birthday, phone }: SignupData) => {
  try {
    const body = { userId, nickname, password, centercode, birthday, phoneNumber: phone };
    const data = await axiosApi.post('/users/signup', body);
    
    return data;
  }
  catch(e) {
    console.log(e);
  }
};

export const login = async( id: string, password: string ) => {
  try {
    const body = { userId: id, password };
    const data = await axiosApi.post('/login', body);
    const accessToken = data.headers.authorization;
    setCookie('ari_login', accessToken?.split(' ')[1]);
    return data.data;
  }
  catch(e) {
    if(axios.isAxiosError(e)) {
      if(e.response?.data?.message === 'Unauthorized') return e.response.data;
      console.log(e);
    }
    else console.log(e);
  }
};

export const firstLogin = async({ genderType, ageType, categories }: Info) => {
  try {
    const body = { genderType, ageType, categories };
    const data = await axiosApi.patch('/user/firstlogin', body);
    return data;
  }
  catch(e) {
    console.log(e);
  }
};

export const mypage = async() => {
  try {
    const { data: { data } } = await axiosApi.get('/mypage');
    return data;
  }
  catch(e) {
    if(axios.isAxiosError(e)) {
      return e.response?.data;
    }
    console.log(e);
  }
};

export const modifyMypage = async( patch: SettingData, image: File | string ) => {
  try {
    const body = new FormData();
    const blob = new Blob([JSON.stringify(patch)], {
      type: 'application/json'
    });
    if(typeof image === 'string') {
      patch.profileImage = image;
    }
    else body.append('image', image);
    console.log(patch);
    body.append('patch', blob);
    const data = await axiosApi.patch('/mypage/patch', body);
    return data;
  }
  catch(e) {
    console.log(e);
  }
};

export const myPosts = async() => {
  try {
    const { data: { data } } = await axiosApi.get('/mypage/myfeeds');
    return data;
  }
  catch(e) {
    console.log(e);
  }
};

export const myComments = async() => {
  try {
    const { data: { data } } = await axiosApi.get('/mypage/mycomments');
    return data;
  }
  catch(e) {
    console.log(e);
  }
};

export const myLikes = async() => {
  try {
    const { data: { data } } = await axiosApi.get('/mypage/myfeedlikes');
    return data;
  }
  catch(e) {
    console.log(e);
  }
};

export const deleteAccount = async() => {
  try {
    const data = await axiosApi.delete('/mypage/signout');
    return data;
  }
  catch(e) {
    console.log(e);
  }
};

export const changePassword = async() => {
  try {
    const data = await axiosApi.patch('/mypage/patch/password');
    return data;
  }
  catch(e) {
    console.log(e);
  }
}