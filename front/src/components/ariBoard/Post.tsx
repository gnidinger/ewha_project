import { useNavigate } from 'react-router-dom';
import { PostType } from '../../pages/AriBoard';
import Card from '@mui/material/Card';
import Typography from '@mui/material/Typography';
import FavoriteIcon from '@mui/icons-material/Favorite';
import MessageOutlinedIcon from '@mui/icons-material/MessageOutlined';
import { pink } from '@mui/material/colors';
import { INTERESTS, interestsObject } from '../common/interestsList';

interface Props {
  postData: PostType
}

const Post = ({ postData }: Props) => {
  const navigation = useNavigate();

  const handleClick = () => {
    navigation(`/post/${postData.feedId}`);
  };

  return(
    <Card variant='outlined' onClick={handleClick} sx={{ padding: '1.4rem', textAlign: 'left' }}>
      <Typography sx={{ fontSize: 10 }}>
        {postData.categories.map((t) => (
          <span key={t}>#{interestsObject[t as keyof INTERESTS]} </span>
        ))}
      </Typography>
      <Typography sx={{ fontSize: 16 }} gutterBottom>
        {postData.title}
      </Typography>
      <Typography sx={{ fontSize: 12 }} gutterBottom>
        {postData.body.length > 36 ? postData.body.substring(0, 35) + '...' : postData.body}
      </Typography>
      <Typography sx={{ fontSize: 12, textAlign: 'right' }}>
        <FavoriteIcon sx={{ fontSize: 16, color: pink[500], verticalAlign: 'middle' }} />
        {` ${postData.likeCount} `}
        <MessageOutlinedIcon sx={{ fontSize: 16, verticalAlign: 'middle' }} />
        {` ${postData.commentCount} `}
      </Typography>
    </Card>
  );
};

export default Post;