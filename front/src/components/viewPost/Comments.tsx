import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Avatar from '@mui/material/Avatar';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import { Comment } from '../../pages/View';
import { getCookie } from '../../api/cookie';
import { writeComment } from '../../api/comment';

interface Props {
  feedId: string,
  commentsData: Comment[],
  rerender: () => void
}

const Comments = ({ feedId, commentsData, rerender }: Props) => {
  const handleSubmit = async(event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    await writeComment(feedId, String(formData.get('comment')));
    rerender();
  };

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
        <Box component='form' onSubmit={handleSubmit} sx={{ display: 'grid', gridTemplateColumns: 'auto 5rem' }}>
          <TextField
            fullWidth
            label='댓글을 남겨보세요.'
            name='comment'
          />
          <Button type='submit' variant='contained' sx={{ ml: 1 }}>등록</Button>
        </Box>
      }
    </Box>
  );
};

export default Comments;