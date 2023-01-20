import { useState } from 'react';
import styled from 'styled-components';
import { Profile } from '../components/mypage';

interface MyInformation {
  name: string,
  arigauge: number,
  posts: Object,
  comments: Object,
  likes: Object
  questions: Object
}

const Mypage = () => {
  const [myInfo, setMyInfo] = useState<MyInformation>({
    name: '홍길동',
    arigauge: 36.5,
    posts: {},
    comments: {},
    likes: {},
    questions: {}
  });

  return(
    <StPageWrapper>
      <StMainWrapper>
        <Profile name={myInfo.name} arigauge={myInfo.arigauge}/>
      </StMainWrapper>
    </StPageWrapper>
  );
};

const StPageWrapper = styled.div`
  display: grid;
  place-items: center;
  height: 100vh;
`;

const StMainWrapper = styled.div`
  width: 24rem;
  margin: 0 auto;
`;

export default Mypage;