import { useNavigate } from 'react-router-dom';
import Box from '@mui/material/Box';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import { deleteAccount } from '../../api/user';
import { removeCookie } from '../../api/cookie';

const DeleteAccount = () => {
  const navigation = useNavigate();

  const handleClickBtn = async() => {
    await deleteAccount();
    removeCookie('ari_login');
    navigation('/login');
  };

  return(
    <Box sx={{ textAlign: 'center' }}>
      <DialogTitle>회원 탈퇴</DialogTitle>
      <DialogContent>
        <DialogContentText mb={3}>탈퇴 후에는 기록이 모두 삭제되어 복구할 수 없습니다.</DialogContentText>
        <TextField
          name='password'
          id='password'
          type='password'
          variant='standard'
          label='비밀번호 입력'
          autoFocus
        />
        <Box mt={3}>
          <Button onClick={handleClickBtn} variant='contained'>탈퇴하기</Button>
        </Box>
      </DialogContent>
    </Box>
  );
};

export default DeleteAccount;