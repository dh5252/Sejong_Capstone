import { Link } from "react-router-dom"
import styles from './intro.module.css'

export default function Intro() {

    return (
        <div className="flex flex-col w-full h-[100dvh] gap-5 justify-center items-center">
            <p className={styles.title}>당신만을 위한 <br />맞춤 레스토랑 리뷰 서비스</p>
            <img src={"/icons/logo-transparent.png"} width="250px" height="250px" alt="먹자VIEW" />
            <Link className={styles.googleBtn} to="https://mukjaview.kro.kr/oauth2/authorization/google?redirect_uri=http://localhost:3000/signup">
                {/* <Link to="https://mukjaview.kro.kr/oauth2/authorization/google?redirect_uri=https://mukjaview.kro.kr/signup"> */}
                <div className={styles.icon}>
                    <img src="/g-logo.png" alt="Google icon" />
                </div>
                구글로 시작하기
            </Link>
        </div>
    )
}