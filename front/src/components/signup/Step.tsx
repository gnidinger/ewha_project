import { useState, useEffect, useRef } from 'react';
import TextField from '@mui/material/TextField';
import Grid from '@mui/material/Grid';
import Button from '@mui/material/Button';
import { getCertificationNumber, verify } from '../../api/user';
import { CheckList } from './SignupForm';

interface Props {
  changeBtn: React.Dispatch<React.SetStateAction<boolean>>,
  checkError?: CheckList,
  idError?: number
}

const HELPER_TEXT = {
  id: ['이미 사용중인 아이디입니다.', '6~12자의 영문, 숫자만 사용 가능합니다.'],
  nickname: ['이미 사용중인 별명입니다'],
  password: ['8~16자 영문, 숫자, 특수문자(@$!%*?&)를 포함하여야 합니다.']
};

export const FirstStep = ({ changeBtn, checkError, idError }: Props) => {
  const [id, setId] = useState<string>('');
  const [nickname, setNickname] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [reenterPassword, setReenterPassword] = useState<string>('');
  const [confirm, setConfirm] = useState<boolean>(true);

  const handleChangeId = (event: React.ChangeEvent<HTMLInputElement>) => {
    setId(event.currentTarget.value);
  };

  const handleChangeNickname = (event: React.ChangeEvent<HTMLInputElement>) => {
    setNickname(event.currentTarget.value);
  };

  const handleChangePassword = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(event.currentTarget.value);
  };

  const handleChangeReenterPassword = (event: React.ChangeEvent<HTMLInputElement>) => {
    if(event.currentTarget.value !== password) setConfirm(false);
    else setConfirm(true);
    setReenterPassword(event.currentTarget.value);
  };

  useEffect(()=> {
    if(id !== '' && nickname !== '' && password !== '' && reenterPassword !== '' && confirm === true) changeBtn(true);
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
          error={checkError?.userId}
          variant="standard"
          helperText={checkError?.userId && HELPER_TEXT.id[idError as number]}
          onChange={handleChangeId}
        />
      </Grid>
      <Grid item xs={12}>
        <TextField
          autoComplete="nickname"
          name="nickname"
          required
          fullWidth
          id="nickname"
          label="별명"
          error={checkError?.nickname}
          helperText={checkError?.nickname && HELPER_TEXT.nickname}
          variant="standard"
          onChange={handleChangeNickname}
        />
      </Grid>
      <Grid item xs={12}>
        <TextField
          required
          fullWidth
          id="password"
          label="비밀번호(영문, 숫자, 특수문자(@$!%*?&) 사용 가능)"
          name="password"
          type="password"
          error={checkError?.password}
          helperText={checkError?.password && HELPER_TEXT.password}
          variant="standard"
          onChange={handleChangePassword}
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
          onChange={handleChangeReenterPassword}
        />
      </Grid>
    </Grid>
  );
};

export const SecondStep = ({ changeBtn }: Props) => {
  const [phoneNumber, setPhoneNumber] = useState<string>('');
  const [disabledCodeInput, setDisabledCodeInput] = useState<boolean>(true);
  const [verificationCode, setVerificationCode] = useState<string>('');
  const [codeText, setCodeText] = useState<string>('');

  const codeRef = useRef<HTMLDivElement>(null);

  const getCode = async() => {
    await getCertificationNumber(phoneNumber);
    setDisabledCodeInput(false);
  };

  const verification = async() => {
    const inputValue = (codeRef.current?.childNodes[1].childNodes[0] as HTMLInputElement).value;
    const data = await verify(phoneNumber, inputValue);
    if(data === 200) {
      setCodeText('인증되었습니다.');
      changeBtn(true);
    }
    else {
      setCodeText('인증번호가 일치하지 않습니다.');
    }
  };

  const onPhoneHandler = (event: React.ChangeEvent<HTMLInputElement>): void => {
    setPhoneNumber(event.currentTarget.value);
  };

  const onCodeHandler = (event: React.ChangeEvent<HTMLInputElement>): void => {
    setVerificationCode(event.currentTarget.value);
  };

  return(
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <TextField
          name='centercode'
          fullWidth
          id='centercode'
          label='기관코드'
          variant="standard"
          autoFocus
        />
      </Grid>
      <Grid item xs={12}>
        <TextField
          autoComplete="birthday"
          name="birthday"
          required
          fullWidth
          defaultValue='1900-01-01'
          id="birthday"
          label="생년월일"
          type='date'
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
          onClick={getCode}
          disabled={phoneNumber.length === 11 ? false : true}
        >인증번호 받기</Button>
      </Grid>
      <Grid item xs={12}>
        <TextField
          required
          name="code"
          label="인증번호"
          id="code"
          variant="standard"
          helperText={codeText}
          disabled={disabledCodeInput}
          ref={codeRef}
          style={{ width: '16rem' }}
          onChange={onCodeHandler}
        />
        <Button
          size='small'
          color='secondary'
          variant="contained"
          disableElevation
          style={{ width: '2.8rem', minWidth: '0' }}
          disabled={verificationCode.length ? false : true}
          onClick={verification}
        >확인</Button>
      </Grid>
    </Grid>
  );
};