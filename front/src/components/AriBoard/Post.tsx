import styled from 'styled-components';
import { PostType } from '../../pages/AriBoard';

interface Props {
  postData: PostType
}

const Post = ({ postData }: Props) => {
  return(
    <PostWrapper>
      <Tag>{postData.tag.map((t) => (
        <span>#{t} </span>
      ))}</Tag>
      <Info>
        <Photo src='img/1.jpg' /> {postData.userName}
      </Info>
      <Content>{postData.content}</Content>
      <SubContent></SubContent>
    </PostWrapper>
  );
};

const PostWrapper = styled.div`
  display: grid;
  grid-template-rows: 3.1rem 5.2rem 5.8rem 3.7rem;
  width: 100%;
  height: 17.8rem;
  border-bottom: 0.1rem solid grey;
`;

const Tag = styled.div`
  width: 100%;
  padding-left: 1.3rem;
  line-height: 3.1rem;
  vertical-align: middle;
  font-size: 1.1rem;
`;

const Info = styled.div`
  width: 100%;
  padding-left: 1.3rem;
  font-size: 1.2rem;
`;

const Content = styled.div`
  width: 100%;
  padding: 1.3rem;
  font-size: 1.2rem;
`;

const SubContent = styled.div`
  width: 100%;
`;

const Photo = styled.img`
  width: 5rem;
  height: 5rem;
  border-radius: 50%;
  vertical-align: middle;
  margin-right: 1rem;
`;

export default Post;