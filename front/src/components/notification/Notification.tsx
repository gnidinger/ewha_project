import Box from '@mui/material/Box';
import { MAIN_COLOR } from '../../style/palette';

interface Props {
  type: string,
  body: string
}

const MainBoxStyle = {
  width: '90%',
  padding: '1rem',
  border: `0.1rem solid ${MAIN_COLOR}`,
  borderRadius: '1rem',
  margin: '0.4rem auto'
}

const Notification = ({ type, body }: Props) => {
  const shortBody = body ? body.length > 10 ? body.substring(0, 9) + '...' : body : null;

  return(
    <Box sx={MainBoxStyle}>
      {type === 'COMMENT' &&
        `회원님의 게시글 '${shortBody}'에 새 댓글이 달렸습니다.`
      }
      {type === 'LIKE' &&
        `회원님의 '${shortBody}'에 공감합니다.`
      }
    </Box>
  );
};

export default Notification;