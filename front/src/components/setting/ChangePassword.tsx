import Box from '@mui/material/Box';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';

const ChangePassword = () => {
  return(
    <Box sx={{ textAlign: 'center' }}>
      <DialogTitle>비밀번호 변경</DialogTitle>
      <DialogContent>
        <TextField name='current-password' variant='standard' label='현재 비밀번호' type='password' />
        <TextField name='new-password' variant='standard' label='새 비밀번호' type='password' />
        <TextField name='password-confirm' variant='standard' label='비밀번호 확인' type='password' />
        <Box mt={3}>
          <Button fullWidth variant='contained'>변경하기</Button>
        </Box>
      </DialogContent>
    </Box>
  );
};

export default ChangePassword;