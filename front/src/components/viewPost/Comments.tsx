import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Avatar from '@mui/material/Avatar';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import { Comment } from '../../pages/View';
import { getCookie } from '../../api/cookie';

interface Props {
  commentsData: Comment[]
}

const Comments = ({ commentsData }: Props) => {
  return(
    <Box sx={{ padding: 2 }}>
      <Typography sx={{ mb: 2 }}>댓글 {commentsData.length}</Typography>
      {commentsData.map((comment: Comment) => (
        <Box key={comment.commentId} sx={{ mb: 2 }}>
          <Avatar sx={{ float: 'left', width: 32, height: 32 }} />
          <Typography sx={{ lineHeight: '32px', verticalAlign: 'middle', ml: 5 }} gutterBottom>{comment.userInfo.nickname}</Typography>
          <Typography>{comment.body}</Typography>
        </Box>
      ))}
      {getCookie('ari_login') &&
        <Box sx={{ display: 'grid', gridTemplateColumns: 'auto 5rem' }}>
          <TextField
            fullWidth
            label='댓글을 남겨보세요.'
          />
          <Button variant='contained' sx={{ ml: 1 }}>등록</Button>
        </Box>
      }
    </Box>
  );
};

export default Comments;