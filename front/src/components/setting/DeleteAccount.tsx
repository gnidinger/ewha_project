import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';

const mainBoxStyle = {
  position: 'fixed',
  width: '100%',
  top: '6rem',
  textAlign: 'center',
  backgroundColor: 'white',
  padding: '4rem 0'
}

const DeleteAccount = () => {
  return(
    <Box sx={mainBoxStyle}>
      <Typography sx={{ fontSize: 16, m: 3, fontWeight: '600' }}>회원 탈퇴</Typography>
      <Typography mb={4}>탈퇴 후에는 기록이 모두 삭제되어 복구할 수 없습니다.</Typography>
      <Typography gutterBottom>비밀번호</Typography>
      <TextField
        name='password'
        id='password'
        type='password'
        autoFocus
      />
      <Box mt={3}>
        <Button variant='contained'>탈퇴하기</Button>
      </Box>
    </Box>
  );
};

export default DeleteAccount;