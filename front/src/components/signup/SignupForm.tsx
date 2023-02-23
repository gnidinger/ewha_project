import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FirstStep, SecondStep } from './Step';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import { check, signup } from '../../api/user';

export interface SignupData {
  userId?: string,
  nickname?: string,
  password?: string,
  centercode?: string,
  birthday?: string,
  phone?: string
}

export interface CheckList {
  userId: boolean,
  nickname: boolean,
  password: boolean
}

interface ResponseData {
  fieldErrors: [] | null,
  message: string,
  status: number,
}

interface FieldErrors {
  field: string,
  reason: string,
  rejectedValue: string
}

const SignupForm = () => {
  const [step, setStep] = useState<number>(1);
  const [btnState, setBtnState] = useState<boolean>(false);
  const [signupData, setSignupData] = useState<SignupData>({});
  const [checkError, setCheckError] = useState<CheckList>({ userId: false, nickname: false, password: false });
  const [idError, setIdError] = useState<number>(0);

  const navigation = useNavigate();

  const handleSubmit = async(event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const newSignupData = { ...signupData };
    if(step === 1) {
      newSignupData.userId = data.get('id') as string;
      newSignupData.nickname = data.get('nickname') as string;
      newSignupData.password = data.get('password') as string;
      const checking = await check(newSignupData);
      if(checking === 200) {
        setSignupData(newSignupData);
        setStep(step + 1);
        // setBtnState(false);
      }
      else {
        const newCheckError = { userId: false, nickname: false, password: false };
        checking.forEach((field: FieldErrors) => {
          newCheckError[field.field as keyof CheckList] = true;
          if(field.field === 'userId') {
            if(field.reason === '존재하는 아이디 입니다.') setIdError(0);
            else setIdError(1);
          }
        });
        setCheckError(newCheckError);
      }
    }
    if(step === 2) {
      newSignupData.centercode = data.get('centercode') as string;
      newSignupData.birthday = data.get('birthday') as string;
      newSignupData.phone = data.get('phone') as string;
      const signupResult = signup(newSignupData);
      setSignupData(newSignupData);
      setStep(step + 1);
    }
    if(step === 3) {
      navigation('/login');
    }
  };

  return(
    <Box
      sx={{
        marginTop: 6,
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
      }}
    >
      {(step === 1 || step === 2) &&
        <Typography>서비스 이용을 위해 정보를 입력해주세요.</Typography>
      }
      {step === 3 && 
        <>
          <Typography>가입이 완료되었어요.</Typography>
          <Typography>가입하신 아이디로 로그인 해주세요!</Typography>
        </>
      }
      <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 7 }}>
      {step === 1 && <FirstStep changeBtn={setBtnState} checkError={checkError} idError={idError} />}
      {step === 2 && <SecondStep changeBtn={setBtnState} />}
        <Button
          disabled={btnState ? false : true}
          type="submit"
          fullWidth
          variant="contained"
          disableElevation
          sx={{ mt: 7, mb: 2 }}
        >
          {(step === 1 || step === 2) && '다 음'}
          {step === 3 && '완 료'}
        </Button>
      </Box>
    </Box>
  );
};

export default SignupForm;