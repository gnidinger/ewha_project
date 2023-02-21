import { useState } from 'react';
import Box from '@mui/material/Box';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import { changePassword } from '../../api/user';

interface Props {
  show: React.Dispatch<React.SetStateAction<boolean>>
}

export interface PasswordData {
  oldPassword: string,
  newPassword: string,
  newPasswordRepeat: string
}

const ChangePassword = ({ show }: Props) => {
  const [passwordError, setPasswordError] = useState<boolean>(false);
  const [newPasswordError, setNewPasswordError] = useState<boolean>(false);
  const [passwordConfirmError, setPasswordConfirmError] = useState<boolean>(false);

  const handleSubmit = async(event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setPasswordError(false);
    setNewPasswordError(false);
    setPasswordConfirmError(false);
    const passwordData: PasswordData = { oldPassword: '', newPassword: '', newPasswordRepeat: '' };
    const formData = new FormData(event.currentTarget);
    passwordData.oldPassword = String(formData.get('current-password'));
    passwordData.newPassword = String(formData.get('new-password'));
    passwordData.newPasswordRepeat = String(formData.get('password-confirm'));
    const data = await changePassword(passwordData);
    if(data === '') show(false);
    else if(data === 'password') setPasswordError(true);
    else if(data === 'newPassword') setNewPasswordError(true);
    else if(data === 'passwordConfirm') setPasswordConfirmError(true);
  };

  return(
    <Box component='form' onSubmit={handleSubmit} sx={{ textAlign: 'center' }}>
      <DialogTitle>비밀번호 변경</DialogTitle>
      <DialogContent>
        <TextField 
          margin='dense'
          fullWidth
          name='current-password'
          variant='standard'
          label='현재 비밀번호'
          type='password'
          error={passwordError}
          helperText={passwordError && '비밀번호가 일치하지 않습니다.'}
        />
        <TextField
          margin='dense'
          fullWidth
          name='new-password'
          variant='standard'
          label='새 비밀번호'
          type='password'
          error={newPasswordError}
          helperText={newPasswordError && '8~16자 영문, 숫자, 특수문자(@$!%*?&)를 포함하여야 합니다.'}
        />
        <TextField
          margin='dense'
          fullWidth
          name='password-confirm'
          variant='standard'
          label='비밀번호 확인'
          type='password'
          error={passwordConfirmError}
          helperText={passwordConfirmError && '비밀번호가 다릅니다.'}
        />
        <Box mt={3}>
          <Button type='submit' fullWidth variant='contained'>변경하기</Button>
        </Box>
      </DialogContent>
    </Box>
  );
};

export default ChangePassword;