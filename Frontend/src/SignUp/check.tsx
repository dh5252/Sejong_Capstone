import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import Loading from "../components/Loading";
import { useQuery } from "react-query";
import { AxiosError } from "axios";
import { getUserInfo } from "../apis/userInfo";

interface CheckProps {
  setStep: (step: number)=>void;
  setIdentifier: (identifier: string)=>void;
}

export interface UserInfo {
  mukbti?: string;
  oauthIdentifier: string;
  email?: string;
  realname?: string;
  nickname: string;
  neutralImageUrl?: string;
  smileImageUrl?: string;
  sadImageUrl?: string;
  init: boolean;
}

export default function Check({ setStep, setIdentifier }: CheckProps){

    let navigate = useNavigate();

    let [searchParams, setSearchParams] = useSearchParams();

    const { data, isError, error, isLoading } = useQuery<UserInfo, AxiosError>(
      "userInfo",
      getUserInfo,
      {
        onSuccess: (userInfo)=>{
          setIdentifier(userInfo.oauthIdentifier);
          if(userInfo.init === false || searchParams.get('edit') === 'true'){
            setStep(1);
          }
          else{
            navigate('/mypage')
          }
        }
      }
    );

    if (isLoading) return <Loading />
    if (isError) return <div>Error발생 {error.message}</div>

    return null;
}