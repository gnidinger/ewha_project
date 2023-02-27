import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import styled from 'styled-components';
import { Header } from '../components/common';
import { Post, Subjects } from '../components/ariBoard';
import Fab from '@mui/material/Fab';
import Pagination from '@mui/material/Pagination';
import { getLatest, getPostsBySubject } from '../api/post';

export interface PostType {
  feedId: number,
  categories: string[],
  title: string,
  body: string,
  likeCount: number,
  commentCount: number,
  createdAt: string
}

const AriBoard = () => {
  const [isLatest, setIsLatest] = useState<boolean>(true);
  const [currentPage, setCurrentPage] = useState<number>(1);
  const [totalPage, setTotalPage] = useState<number>(1);
  const [currentSubject, setCurrentSubject] = useState<string>('HEALTH');
  const [posts, setPosts] = useState<PostType[]>([]);

  const navigation = useNavigate();
  const location = useLocation();

  const changeTopic = (event: React.MouseEvent<HTMLElement>): void => {
    setIsLatest(Boolean((event.target as any).id));
    setCurrentPage(1);
  };

  const getPosts = async(): Promise<void> => {
    let data;
    if(isLatest) data = await getLatest(currentPage);
    else data = await getPostsBySubject(currentSubject, currentPage);
    setTotalPage(data.pageInfo ? data.pageInfo.totalPages : 1);
    setPosts(data.data);
  };

  const handlePage = (event: React.ChangeEvent<unknown>, page: number) => {
    setCurrentPage(page);
  };

  useEffect(() => {
    getPosts();
    window.scrollTo(0, 0);
  }, [isLatest, currentSubject, currentPage]);

  useEffect(() => {
    location.state && setIsLatest(location.state.latest);
  }, []);

  return(
    <StPageWrapper>
      <Header>아리공간</Header>
      <Tab>
        <Topic id='1' selected={isLatest} onClick={changeTopic}>최신글</Topic>
        <Topic id='' selected={!isLatest} onClick={changeTopic}>주제별</Topic>
      </Tab>
      {!isLatest &&
        <Subjects current={currentSubject} change={setCurrentSubject} />
      }
      {posts.map((post, index) => (
        <Post key={index} postData={post} />
      ))
      }
      <Pagination
        count={totalPage}
        sx={{ display: 'grid', placeItems: 'center', margin: '1rem 0' }}
        onChange={handlePage}
        page={currentPage}
      />
      <Fab
        size='medium'
        variant='extended'
        aria-label="add"
        sx={{ position: 'fixed', bottom: 16, right: 16 }}
        onClick={() => navigation('/write')}
      >
        글쓰기
      </Fab>
    </StPageWrapper>
  );
};

interface topicType {
  selected: boolean
}

const StPageWrapper = styled.div`
  text-align: center;
`;

const Tab = styled.div`
  cursor: pointer;
  display: grid;
  grid-template-columns: 1fr 1fr;
  width: 100%;
  height: 3.6rem;
  border-bottom: 0.1rem solid grey;
`;

const Topic = styled.div<topicType>`
  height: 98%;
  font-size: 1.2rem;
  text-align: center;
  line-height: 3.6rem;
  vertical-align: middle;
  border-right: 0.1rem solid grey;
  color: ${({ selected }) => selected ? 'white' : 'black'};
  background-color: ${({ selected }) => selected ? 'grey' : 'white'};
`;

export default AriBoard;