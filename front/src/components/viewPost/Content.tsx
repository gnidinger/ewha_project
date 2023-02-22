import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Avatar from '@mui/material/Avatar';
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderRoundedIcon from '@mui/icons-material/FavoriteBorderRounded';
import MessageOutlinedIcon from '@mui/icons-material/MessageOutlined';
import { pink } from '@mui/material/colors';
import { INTERESTS, interestsObject } from '../common/interestsList';
import { Post } from '../../pages/View';
import { like, dislike } from '../../api/post';

interface Props {
  postData: Post,
  rerender: () => void
}

export const heartIconStyle = {
  fontSize: 16, color: pink[500], verticalAlign: 'middle'
}

const Content = ({ postData, rerender }: Props) => {
  const clickLike = async() => {
    await like(postData.feedId);
    rerender();
  };

  const clickDislike = async() => {
    await dislike(postData.feedId);
    rerender();
  };

  return(
    <Box sx={{ padding: 3 }}>
      <Typography sx={{ fontSize: 18 }} gutterBottom>{postData.title}</Typography>
      <Avatar src={postData.userInfo.profileImage} sx={{ float: 'left', width: 36, height: 36 }} />
      <Typography sx={{ lineHeight: '36px', verticalAlign: 'middle', ml: 6 }} gutterBottom>{postData.userInfo.nickname}</Typography>
      <Typography sx={{ fontSize: 11 }} gutterBottom>{postData.createdAt.substring(0, 10)}</Typography>
      <Typography sx={{ fontSize: 12 }} gutterBottom>
        {postData.categories.map((t) => (
          <span key={t}>#{interestsObject[t as keyof INTERESTS]} </span>
        ))}
        {postData.isLiked ?
        <FavoriteIcon onClick={clickDislike} sx={heartIconStyle} /> :
        <FavoriteBorderRoundedIcon onClick={clickLike} sx={heartIconStyle} />
        }
        {` ${postData.likeCount} `}
        <MessageOutlinedIcon sx={{ fontSize: 16, verticalAlign: 'middle' }} />
        {` ${postData.comments.length} `}
      </Typography>
      {postData.imagePath &&
        <Box sx={{ width: '100%', maxWidth: '32rem' }}>
          <img style={{ width: '100%' }} src={postData.imagePath} />
        </Box>
      }
      <Box sx={{ width: '100%' }}>
        <p style={{ wordBreak: 'break-all', fontSize: '1.1rem' }}>{postData.body}</p>
      </Box>
    </Box>
  );
};

export default Content;