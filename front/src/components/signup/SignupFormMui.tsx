import { useState, createContext } from 'react';
import { FirstStep, SecondStep } from './Step';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';

const SignupForm = () => {
  const [step, setStep] = useState<number>(1);
  const [btnState, setBtnState] = useState<boolean>(false);

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    console.log({
      id: data.get('id'),
      password: data.get('password'),
    });
    setStep(step + 1);
    setBtnState(false);
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
      <Typography>
        서비스 이용을 위해 정보를 입력해주세요
      </Typography>
      <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 7 }}>
      {step === 1 && <FirstStep changeBtn={setBtnState} />}
      {step === 2 && <SecondStep changeBtn={setBtnState} />}
        <Button
          disabled={btnState ? false : true}
          type="submit"
          fullWidth
          variant="contained"
          disableElevation
          sx={{ mt: 7, mb: 2 }}
        >
          다 음
        </Button>
      </Box>
    </Box>
  );
};

export default SignupForm;