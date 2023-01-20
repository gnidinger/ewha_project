import styled from 'styled-components';
import { Photo } from '../common';

const Completion = () => {
  return(
    <CompletionWrapper>
      <StPhotoWrapper>
        <Photo />
      </StPhotoWrapper>
      <StComment>가입이 완료되었어요.</StComment>
      <StComment>가입하신 아이디로 로그인 해주세요!</StComment>
    </CompletionWrapper>
  );
};

const CompletionWrapper = styled.div`
  width: 100%;
`;

const StPhotoWrapper = styled.div`
  position: relative;
  width: 100%;
  height: 22rem;
  margin-bottom: 6.8rem;
`;

const StComment = styled.div`
  width: 100%;
  margin: 0 auto 1.8rem auto;
  font-size: 1.45rem;
  text-align: center;
`;

export default Completion;