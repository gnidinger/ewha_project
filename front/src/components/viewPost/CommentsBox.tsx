import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Comment from './Comment';
import { CommentData } from '../../pages/View';
import { getCookie } from '../../api/cookie';
import { writeComment } from '../../api/comment';

interface Props {
  feedId: string,
  commentsData: CommentData[],
  rerender: () => void
}

const CommentsBox = ({ feedId, commentsData, rerender }: Props) => {
  const handleSubmit = async(event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    await writeComment(feedId, String(formData.get('comment')));
    rerender();
  };

  return(
    <Box sx={{ padding: 2 }}>
      <Typography sx={{ mb: 2 }}>댓글 {commentsData.length}</Typography>
      {commentsData.map((comment: CommentData) => (
        <Comment key={comment.commentId} comment={comment} rerender={rerender} />
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

export default CommentsBox;