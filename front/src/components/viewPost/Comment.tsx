import Box from '@mui/material/Box';
import Avatar from '@mui/material/Avatar';
import Typography from '@mui/material/Typography';
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderRoundedIcon from '@mui/icons-material/FavoriteBorderRounded';
import { CommentData } from '../../pages/View';
import { heartIconStyle } from './Content';
import { like, dislike } from '../../api/comment';

interface Props {
  comment: CommentData, 
  rerender: () => void
}

const Comment = ({ comment, rerender }: Props) => {
  const clickLike = async() => {
    await like(comment.commentId);
    rerender();
  };

  const clickDislike = async() => {
    await dislike(comment.commentId);
    rerender();
  };

  return(
    <Box sx={{ mb: 2 }}>
      <Avatar src={comment.userInfo.profileImage} sx={{ float: 'left', width: 32, height: 32 }} />
      <Typography sx={{ lineHeight: '32px', verticalAlign: 'middle', ml: 5 }} gutterBottom>{comment.userInfo.nickname}</Typography>
      <Typography>{comment.body}</Typography>
      {comment.isLikedComment ?
      <FavoriteIcon onClick={clickDislike} sx={heartIconStyle} /> :
      <FavoriteBorderRoundedIcon onClick={clickLike} sx={heartIconStyle} />
      } {comment.likeCount}
    </Box>
  );
};

export default Comment;