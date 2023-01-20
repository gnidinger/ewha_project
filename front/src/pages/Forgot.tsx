import { useState } from 'react';
import styled from 'styled-components';
import { Logo } from '../components/common';
import { IdForm, PasswordForm } from '../components/forgot';

const Forgot = () => {
  const [formSwitch, setFormSwitch] = useState<boolean>(true);

  const changeForm = (e: React.MouseEvent<HTMLElement>): void => {
    if((e.target as any).id === 'id') setFormSwitch(true);
    if((e.target as any).id === 'password') setFormSwitch(false);
  };

  return(
    <StPageWrapper>
      <StMainWrapper>
        <StLogoWrapper>
          <Logo />
        </StLogoWrapper>
        <StLabel id='id' on={formSwitch} onClick={changeForm}>아이디 찾기</StLabel>
        <StLabel id='password' on={!formSwitch} onClick={changeForm}>비밀번호 찾기</StLabel>
        {formSwitch && 
          <IdForm />
        }
        {!formSwitch &&
          <PasswordForm />
        }
      </StMainWrapper>
    </StPageWrapper>
  );
};

interface LabelProps {
  on: boolean
}

const StPageWrapper = styled.div`
  display: grid;
  place-items: center;
  height: 100vh;
`;

const StMainWrapper = styled.div`
  width: 20rem;
  margin: 0 auto;
`;

const StLogoWrapper = styled.div`
  position: relative;
  width: 100%;
  height: 19rem;
`;

const StLabel = styled.div<LabelProps>`
  cursor: pointer;
  display: inline-block;
  width: 50%;
  padding: 1rem 0;
  text-align: center;
  font-size: 1.2rem;
  font-weight: ${({ on }) => on ? '600' : '500'};
  color: ${({ on }) => on ? '#E95A54' : 'black'};
  border-bottom: ${({ on }) => on ? '1px solid #E95A54' : '1px solid black'};
`;

export default Forgot;