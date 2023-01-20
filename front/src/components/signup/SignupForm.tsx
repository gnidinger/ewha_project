import { useState, useContext } from 'react';
import { SignupContext } from '../../pages/Signup';
import styled from 'styled-components';
import { Input } from '../../style/Input';
import { SubButton } from '../../style/Button';

const SignupForm = () => {
  const { changeBtn } = useContext(SignupContext);

  return(
    <StForm>
      <StComment>서비스 이용을 위해 정보를 입력해주세요</StComment>
      <StIdDiv>
        <StInput placeholder='아이디' />
        <SubButton>중복확인</SubButton>
      </StIdDiv>
      <StInput placeholder='비밀번호' type='password' />
      <StInput placeholder='비밀번호 확인' type='password' />
    </StForm>
  );
};

const StForm = styled.form`
  width: 100%;
`;

const StComment = styled.div`
  width: 58%;
  margin: 0 auto 6.8rem auto;
  font-size: 1.45rem;
`;

const StIdDiv = styled.div`
  position: relative;
  width: 100%;

  button {
    position: absolute;
    width: 6.8rem;
    height: 2.5rem;
    top: 2px;
    right: 2px;
  }
`

const StInput = styled(Input)`
  margin-bottom: 2.6rem;
`;

export default SignupForm;