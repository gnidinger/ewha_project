import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import { interests, age } from '../components/common/interestsList';
import { MAIN_COLOR } from '../style/palette';
import { firstLogin } from '../api/user';

const theme = createTheme({
  palette: {
    primary: {
      main: MAIN_COLOR
    },
    secondary: {
      main: '#F0F0F0'
    }
  }
});

const btnTheme = createTheme({
  palette: {
    primary: {
      main: MAIN_COLOR
    },
    secondary: {
      main: '#F0F0F0'
    }
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          width: '35%',
          margin: '0.5rem'
        }
      }
    }
  }
})

export interface Info {
  genderType: string,
  ageType: string,
  categories: string[]
}

interface Step {
  changeBtn: React.Dispatch<React.SetStateAction<boolean>>,
  setInfo: React.Dispatch<React.SetStateAction<Info>>
}

const StepOne = ({ changeBtn, setInfo }: Step) => {
  const [selected, setSelected] = useState<string>();

  const clickButton = (event: React.MouseEvent<HTMLElement>) => {
    setSelected(event.currentTarget.id);
    setInfo((current) => {
      let newInfo = {...current};
      newInfo.genderType = event.currentTarget.id;
      return newInfo
    });
    changeBtn(true);
  };

  return(
    <div style={{ width: '100%' }}>
      <Button id='MALE' onClick={clickButton} variant={selected === 'MALE' ? 'contained' : 'outlined'}>남자</Button>
      <Button id='FEMALE' onClick={clickButton} variant={selected === 'FEMALE' ? 'contained' : 'outlined'}>여자</Button>
    </div>
  );
};

const StepTwo = ({ changeBtn, setInfo }: Step) => {
  const [selected, setSelected] = useState<string>();

  const clickButton = (event: React.MouseEvent<HTMLElement>) => {
    setSelected(event.currentTarget.id);
    setInfo((current) => {
      let newInfo = {...current};
      newInfo.ageType = event.currentTarget.id;
      return newInfo
    });
    changeBtn(true);
  };

  return(
    <div style={{ width: '100%' }}>
      {age.map((a, index) => (
        <Button id={a} key={a} onClick={clickButton} variant={selected === a ? 'contained' : 'outlined'}>{(index + 1) * 10}대</Button>
      ))}
    </div>
  );
};

const StepThree = ({ changeBtn, setInfo }: Step) => {
  const [selected, setSelected] = useState<string[]>([]);

  const clickButton = (event: React.MouseEvent<HTMLElement>) => {
    let newInfo: Info;
    setInfo((current) => {
      newInfo = {...current};
      if(current.categories.includes(event.currentTarget.id)) newInfo.categories = current.categories.filter((element) => element !== event.currentTarget.id);
      else if(selected.length < 3) newInfo.categories = [...current.categories, event.currentTarget.id];
      setSelected(newInfo.categories);
      if(newInfo.categories.length === 0) changeBtn(false);
      else changeBtn(true);
      return newInfo;
    });
  };

  return(
    <div style={{ width: '100%' }}>
      {interests.map((interest) => (
        <Button id={interest[1]} key={interest[1]} onClick={clickButton} variant={selected.includes(interest[1]) ? 'contained' : 'outlined'}>{interest[0]}</Button>
      ))}
    </div>
  );
};

const SetInfo = () => {
  const [step, setStep] = useState<number>(1);
  const [btnState, setBtnState] = useState<boolean>(false);
  const [info, setInfo] = useState<Info>({ genderType: '', ageType: '', categories: [] });

  const navigation = useNavigate();

  const clickNextStep = () => {
    if(step === 3) {
      firstLogin(info);
      navigation('/');
    }
    setBtnState(false);
    setStep(step + 1);
  };

  return(
    <StPageWrapper>
      <ThemeProvider theme={theme}>
        <Container component="main" style={{ textAlign: 'center' }}>
          {step === 1 && <Typography>당신의 성별은?</Typography>}
          {step === 2 && <Typography>당신의 연령은?</Typography>}
          {step === 3 && <Typography>당신의 관심사는?(최대3개)</Typography>}
          <ThemeProvider theme={btnTheme}>
            <StStep>
              {step === 1 && <StepOne changeBtn={setBtnState} setInfo={setInfo} />}
              {step === 2 && <StepTwo changeBtn={setBtnState} setInfo={setInfo} />}
              {step === 3 && <StepThree changeBtn={setBtnState} setInfo={setInfo} />}
            </StStep>
          </ThemeProvider>
          <Button
            disabled={btnState ? false : true}
            variant='contained'
            disableElevation
            onClick={clickNextStep}
            sx={{ width: '12rem' }}
          >
            {(step === 1 || step === 2) && '다 음'}
            {step === 3 && '완 료'}
          </Button>
        </Container>
      </ThemeProvider>
    </StPageWrapper>
  );
};

const StPageWrapper = styled.div`
  display: grid;
  place-items: center;
  width: 24rem;
  height: 100vh;
  margin: 0 auto;
`;

const StStep = styled.div`
  display: grid;
  place-items: center;
  width: 100%;
  height: 40vh;
  margin: 2rem auto;
`;

export default SetInfo;