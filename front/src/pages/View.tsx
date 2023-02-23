import { useState, useEffect } from 'react';
import { Params, useParams } from 'react-router-dom';
import { getPost } from '../api/post';
import { Header } from '../components/common';
import { Content, CommentsBox } from '../components/viewPost';
import Divider from '@mui/material/Divider';

interface User {
  nickname: string,
  profileImage: string,
  userId: string
}

export interface CommentData {
  body: string,
  commentId: string,
  createdAt: string,
  modifiedAt: string,
  feedId: string,
  likeCount: number,
  isLikedComment: boolean,
  isMyComment: boolean,
  userInfo: User
}

export interface Post {
  feedId: string,
  title: string,
  createdAt: string,
  modifiedAt: string,
  userInfo: User,
  categories: string[],
  likeCount: number,
  body: string,
  comments: CommentData[],
  isLiked: boolean,
  imagePath: string,
  thumbnailPath: string,
  isMyFeed: boolean
}

const View = () => {
  const [postData, setPostData] = useState<Post>();

  const params: Readonly<Params<string>> = useParams<string>();
	const postId = params.post;

  const getPostData = async() => {
    if(postId) {
      const data = await getPost(postId);
      setPostData(data);
    }
  };

  useEffect(() => {
    getPostData();
  }, []);

  return(
    <>
      <Header>아리공간</Header>
      {postData && 
        <Content
          postData={postData}
          rerender={getPostData}
        />
      }
      <Divider />
      {postData && 
        <CommentsBox
          feedId={postData.feedId}
          commentsData={postData.comments ? postData.comments : []}
          rerender={getPostData}
        />
      }
    </>
  );
};

export default View;