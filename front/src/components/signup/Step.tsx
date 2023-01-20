import { useState, useEffect, useRef } from 'react';
import TextField from '@mui/material/TextField';
import Grid from '@mui/material/Grid';
import Button from '@mui/material/Button';

interface Props {
  changeBtn: React.Dispatch<React.SetStateAction<boolean>>
}

export const FirstStep = ({ changeBtn }: Props) => {
  const [id, setId] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [passwordConfirm, setPasswordConfirm] = useState<string>('');
  const [confirm, setConfirm] = useState<boolean>(true);

  const onIdHandler = (event: React.ChangeEvent<HTMLInputElement>) => {
    setId(event.currentTarget.value);
  };

  const onPasswordHandler = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(event.currentTarget.value);
  };

  const confirmPassword = (event: React.ChangeEvent<HTMLInputElement>) => {
    if(event.currentTarget.value !== password) setConfirm(false);
    else setConfirm(true);
    setPasswordConfirm(event.currentTarget.value);
  };

  useEffect(()=> {
    if(id !== '' && password !== '' && passwordConfirm !== '' && confirm === true) changeBtn(true);
  }, [id, password, confirm])

  return(
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <TextField
          autoComplete="id"
          name="id"
          required
          fullWidth
          id="id"
          label="아이디"
          autoFocus
          variant="standard"
          onChange={onIdHandler}
        />
      </Grid>
      <Grid item xs={12}>
        <TextField
          required
          fullWidth
          id="password"
          label="비밀번호"
          name="password"
          type="password"
          variant="standard"
          onChange={onPasswordHandler}
        />
      </Grid>
      <Grid item xs={12}>
        <TextField
          required
          fullWidth
          name="password-confirm"
          label="비밀번호 확인"
          type="password"
          id="password-confirm"
          variant="standard"
          error={confirm ? false : true}
          helperText={!confirm && "비밀번호가 다릅니다."}
          onChange={confirmPassword}
        />
      </Grid>
    </Grid>
  );
};

export const SecondStep = ({ changeBtn }: Props) => {
  const [name, setName] = useState<string>('');
  const [phoneNumber, setPhoneNumber] = useState<string>('');
  const [vericationCode, setVericationCode] = useState<string>('');

  const codeRef = useRef<HTMLDivElement>(null);

  const verication = () => {

  };

  const onPhoneHandler = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPhoneNumber(event.currentTarget.value);
  };

  return(
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <TextField
          autoComplete="name"
          name="name"
          required
          fullWidth
          id="name"
          label="이름"
          autoFocus
          variant="standard"
        />
      </Grid>
      <Grid item xs={12}>
        <TextField
          required
          id="phone"
          label="휴대전화"
          name="phone"
          variant="standard"
          onChange={onPhoneHandler}
        />
        <Button
          size='small'
          color='secondary'
          variant="contained"
          disableElevation
        >인증번호 받기</Button>
      </Grid>
      <Grid item xs={12}>
        <TextField
          required
          name="code"
          label="인증번호"
          id="code"
          variant="standard"
          ref={codeRef}
          style={{ width: '16rem' }}
        />
        <Button
          size='small'
          color='secondary'
          variant="contained"
          disableElevation
          style={{ width: '2.8rem', minWidth: '0' }}
          disabled={vericationCode.length ? false : true}
        >확인</Button>
      </Grid>
    </Grid>
  );
};