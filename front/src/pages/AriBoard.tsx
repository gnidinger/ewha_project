import React, { useState } from 'react';
import styled from 'styled-components';
import { Header } from '../components/common';
import { Post } from '../components/AriBoard';
import { Subject } from '../components/AriBoard';
import { interests } from '../components/signup/interestsList';

export interface PostType {
  tag: string[],
  userId: number,
  userName: string,
  date: Date,
  content: string,
  like: number,
  comment: number
}

//샘플 데이터
const postSample: PostType = {
  tag: ['건강', '가족'],
  userId: 1,
  userName: '닉네임',
  date: new Date('2022-12-29 15:20:20'),
  content: '요즘 코로나 심하던데... 다들 괜찮으세요? 최근에 가족이 걸려서 걱정이네요...',
  like: 2,
  comment: 3
};

const AriBoard = () => {
  const [topic, setTopic] = useState<boolean[]>([true, false, false]);
  const [posts, setPosts] = useState<PostType[]>([postSample]);

  const changeTopic = (e: React.MouseEvent<HTMLElement>): void => {
    const newState = [false, false, false];
    newState[(e.target as any).id] = true;
    setTopic(newState);
  };

  return(
    <StPageWrapper>
      <Header showPrevIcon={false} bottomLine={true}>아리공간</Header>
      <Tab>
        <Topic id='0' selected={topic[0]} onClick={changeTopic}>최신글</Topic>
        <Topic id='1' selected={topic[1]} onClick={changeTopic}>주제별</Topic>
        <Topic id='2' selected={topic[2]} onClick={changeTopic} style={{ border: 'none' }}>내가 쓴 글</Topic>
      </Tab>
      {topic[0] &&
      posts.map((post) => (
        <Post postData={post} />
      ))
      }
      {topic[1] &&
      <SubjectGrid>
        {interests.map((interest) => (
          <Subject>{`#${interest}`}</Subject>
        ))}
      </SubjectGrid>
      }
    </StPageWrapper>
  );
};

interface topicType {
  selected: boolean
}

const StPageWrapper = styled.div`
  display: grid;
  grid-template-rows: 4.6rem 4.6rem auto;
  height: 100vh;
`;

const Tab = styled.div`
  cursor: pointer;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  width: 100%;
  height: 4.6rem;
  border-bottom: 0.1rem solid grey;
`;

const Topic = styled.div<topicType>`
  height: 98%;
  font-size: 1.6rem;
  text-align: center;
  line-height: 4.6rem;
  vertical-align: middle;
  border-right: 0.1rem solid grey;
  color: ${({ selected }) => selected ? 'white' : 'black'};
  background-color: ${({ selected }) => selected ? 'grey' : 'white'};
`;

const SubjectGrid = styled.div`
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-template-rows: 1fr 1fr 1fr 1fr;
  width: 100%;
  height: 100%;
`;

export default AriBoard;