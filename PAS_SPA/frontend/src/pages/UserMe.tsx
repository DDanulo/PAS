import {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';
import {toast} from 'react-toastify';
import axiosSetup from '../api/axiosSetup';
import {useAuth} from '../context/LoggedUserContext';
import {RoleEnum} from '../HandleProtection';

export default function UserMe() {
    const [user, setUser] = useState({
        id: "",
        login: "",
        firstName: "",
        lastName: "",
        email: "",
        isActive: ""
    });
    const {userRole} = useAuth();

    const fetchMe = async () => {
        try {
            const url = `/me`;
            const res = await axiosSetup.get(url);

            const data = res.data;

            setUser(data);
        } catch (e) {
            console.error(e);
            toast.error("Nie udało się pobrać danych użytkownika")
        }
    };

    useEffect(() => {
        const loadData = async () => {
            await fetchMe();
        };
        loadData();
        // console.log(userRole);
    }, [userRole]);

    return (
        <div className="container">
            <div className="list-header">
                <h2>{userRole === RoleEnum.ADMIN ? "Zarządzanie użytkownikami" : "Moje dane"}</h2>
            </div>

            <table className="data-table">
                <thead>
                <tr>
                    <th>Login</th>
                    <th>Imię</th>
                    <th>Nazwisko</th>
                    <th>Email</th>
                    <th>Status</th>
                    <th>Opcje</th>
                </tr>
                </thead>
                <tbody>
                <tr key={user.id}>
                    <td>{user.login}</td>
                    <td>{user.firstName}</td>
                    <td>{user.lastName}</td>
                    <td>{user.email}</td>
                    <td className={user.isActive ? 'status-active' : 'status-blocked'}>
                        {user.isActive ? 'AKTYWNY' : 'ZABLOKOWANY'}
                    </td>
                    <td className="actions-cell">
                        <Link to={`/me/details`} className="link-details">
                            Szczegóły
                        </Link>
                        <Link to={`/me/edit`} className="link-edit">
                            Edytuj
                        </Link>
                    </td>
                </tr>

                </tbody>
            </table>
        </div>
    );
}