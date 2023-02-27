import { useState } from 'react';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import styled from 'styled-components';
import { Input } from '../../style/Input';
import { Button } from '../../style/Button';
import { getFindIdSMS, verifyId } from '../../api/user';

const IdForm = () => {
  const [showCodeInput, setShowCodeInput] = useState<boolean>(false);
  const [userId, setUserId] = useState<string>('');

  const handleSubmit = async(event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if(showCodeInput) {
      const formData = new FormData(event.currentTarget);
      const data = await verifyId(String(formData.get('nickname')), String(formData.get('phone')), String(formData.get('code')));
      if(data.userId) {
        setUserId(data.userId);
      }
      else alert('인증번호가 일치하지 않습니다.');
    }
    else {
      const formData = new FormData(event.currentTarget);
      await getFindIdSMS(String(formData.get('nickname')), String(formData.get('phone')));
      setShowCodeInput(true);
    }
  };

  return(
    userId ?
    <Box mt={5} sx={{ textAlign: 'center' }}>
      <Typography gutterBottom>회원님의 정보와 일치하는 아이디입니다.</Typography>
      <Typography>{userId}</Typography>
    </Box> :
    <StForm onSubmit={handleSubmit}>
      <StInput name='nickname' placeholder='닉네임'></StInput>
      <StInput name='phone' placeholder='휴대전화'></StInput>
      {showCodeInput && <StInput name='code' placeholder='인증번호 입력'></StInput>}
      <Button type='submit'>{showCodeInput ? '확인' : '인증번호 받기'}</Button>
    </StForm>
  );
};

const StForm = styled.form`
  width: 90%;
  margin: 3.6rem auto;
`;

const StInput = styled(Input)`
  margin-bottom: 2.2rem;
`;

export default IdForm;