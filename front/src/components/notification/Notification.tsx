import Box from '@mui/material/Box';
import { MAIN_COLOR } from '../../style/palette';

interface Props {
  type: string,
  feed: string,
  comment: string
}

const MainBoxStyle = {
  width: '90%',
  padding: '1rem',
  border: `0.1rem solid ${MAIN_COLOR}`,
  borderRadius: '1rem',
  margin: '0.4rem auto'
}

const Notification = ({ type, feed, comment }: Props) => {
  const shortComment = comment ? comment.length > 10 ? comment.substring(0, 9) + '...' : comment : null;

  return(
    <Box sx={MainBoxStyle}>
      {type === 'COMMENT' &&
        `회원님의 게시글 ${feed}에 새 댓글이 달렸습니다.`
      }
      {type === 'LIKE' && feed &&
        `회원님의 게시글 ${feed}에 공감합니다.`
      }
      {type === 'LIKE' && comment &&
        `회원님의 댓글 ${shortComment}에 공감합니다.`
      }
    </Box>
  );
};

export default Notification;