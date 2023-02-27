import { useState } from 'react';
import styled from 'styled-components';
import { Input } from '../../style/Input';
import { Button } from '../../style/Button';
import { getFindPasswordSMS, verifyPassword } from '../../api/user';

const PasswordForm = () => {
  const [showCodeInput, setShowCodeInput] = useState<boolean>(false);
  const [showChangeForm, setShowChangeForm] = useState<boolean>(false);

  const handleSubmit = async(event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if(showCodeInput) {
      const formData = new FormData(event.currentTarget);
      const data = await verifyPassword(String(formData.get('id')), String(formData.get('phone')), String(formData.get('code')));
      if(data === 200) {
        setShowChangeForm(true);
      }
      else alert('인증번호가 일치하지 않습니다.');
    }
    else {
      const formData = new FormData(event.currentTarget);
      await getFindPasswordSMS(String(formData.get('nickname')), String(formData.get('phone')));
      setShowCodeInput(true);
    }
  };

  const handleChange = async(event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
  };

  return(
    showChangeForm ?
    <StForm onSubmit={handleChange}>
      <StInput name='password' type='password' placeholder='새 비밀번호 입력' />
      <StInput name='password-confirm' type='password' placeholder='새 비밀번호 확인' />
      <Button type='submit'>완료</Button>
    </StForm> :
    <StForm onSubmit={handleSubmit}>
      <StInput name='id' placeholder='아이디' />
      <StInput name='phone' placeholder='휴대전화' />
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

export default PasswordForm;