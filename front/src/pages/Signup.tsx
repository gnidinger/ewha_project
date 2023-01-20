import { useState, createContext } from 'react';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { Main, SignupForm, Certification, Profile, Completion } from '../components/signup';
import { Button } from '../style/Button';

interface ContextType {
  changeBtn: () => void,
  upStep: () => void
}

export const SignupContext = createContext<ContextType>({
  changeBtn: () => {},
  upStep: () => {}
});

const Signup = () => {
  const [btnState, setBtnState] = useState<boolean>(true);
  const [step, setStep] = useState<number>(1);

  const navigation = useNavigate();

  const changeBtnState = (): void => {
    setBtnState(false);
  };

  const goNextStep = (): void => {
    if(step === 5) {
      navigation('/login');
    }
    else {
      setStep(step + 1);
      setBtnState(true);
    }
  };

  return(
    <SignupContext.Provider value={{ changeBtn: changeBtnState, upStep: goNextStep }}>
      <StPageWrapper>
        <StMainWrapper>
          {step === 1 && <Main />}
          {step === 2 && <SignupForm />}
          {step === 3 && <Certification />}
          {step === 4 && <Profile />}
          {step === 5 && <Completion />}
          {step !== 1 &&
          <StButton
            backgroundColor={btnState ? '#D9D9D9' : '#E95A54'}
            onClick={goNextStep}>
              {step === 5 ? '완 료' : '다 음'}
          </StButton>
          }
        </StMainWrapper>
      </StPageWrapper>
    </SignupContext.Provider>
  );
};

const StPageWrapper = styled.div`
  display: grid;
  place-items: center;
  height: 100vh;
`;

const StMainWrapper = styled.div`
  width: 20rem;
  margin: 0 auto;
`;

const StButton = styled(Button)`
  margin: 4rem auto 0 auto;
`;

export default Signup;