import {useEffect, useState} from 'react';
import {useParams, useNavigate} from 'react-router-dom';
import axiosSetup from '../api/axiosSetup.ts';
import {toast} from 'react-toastify';
import {useAuth} from "../context/LoggedUserContext.tsx";
import {RoleEnum} from "../HandleProtection.tsx";

export default function UserDetails() {
    const {id} = useParams();
    const navigate = useNavigate();
    const {userRole}= useAuth();
    const [user, setUser] = useState(null);
    const [reservations, setReservations] = useState([]);
    const [rooms, setRooms] = useState([]);
    const [tab, setTab] = useState<'active' | 'history'>('active');
    useEffect(() => {
        loadData();
    }, [id]);

    const loadData = async () => {
        try {
            let userRes;
            let userReservations;
            if (userRole === RoleEnum.CLIENT){
                userRes = await axiosSetup.get(`/me`);
                userReservations = await axiosSetup.get('/me/reservations');
            }else {
                userRes = await axiosSetup.get(`/users/id/${id}`);
                userReservations = await axiosSetup.get('/reservations');
                userReservations = userReservations.data.filter((r: any) =>
                    r.clientId === id || r.userId === id
                );
            }
            setUser(userRes.data);

            setReservations(userReservations.data);

            const roomInRes = await axiosSetup.get('/rooms');
            setRooms(roomInRes.data);

        } catch (e) {
            toast.error("Nie udało się wczytać danych o rezerwacjach");
            console.error(e);
        }
    };

    const formatDate = (dateVal: any) => {
        if (!dateVal) return "-";
        if (Array.isArray(dateVal)) {
            const [year, month, day, hour, minute] = dateVal;
            return new Date(year, month - 1, day, hour || 0, minute || 0).toLocaleString();
        }
        return new Date(dateVal).toLocaleString();
    };

    const getRoomName = (roomId: string) => {
        const room = rooms.find((r: any) => r.id === roomId || r.roomId === roomId || r._id === roomId);
        return room ? `${room.roomType} (Cena: ${room.basePrice})` : roomId;
    };

    const activeReservations = reservations.filter((r: any) => !r.endTime);
    const endedReservations = reservations.filter((r: any) => r.endTime);

    const displayedReservations = tab === 'active' ? activeReservations : endedReservations;

    if (!user) return <div className="container">Ładowanie...</div>;

    return (
        <div className="container">
            <h2>
                {user.firstName} {user.lastName}
            </h2>
            <div>
                Login: <strong>{user.login}</strong> | Email: <strong>{user.email}</strong> |
                Status: <strong>{user.isActive ? 'AKTYWNY' : 'NIEAKTYWNY'}</strong>
            </div>

            <h3>Rezerwacje Klienta</h3>

            <div style={{marginBottom: '15px'}}>
                <button className="btn" onClick={() => setTab('active')}>
                    Aktualne rezerwacje ({activeReservations.length})
                </button>
                <button className="btn" onClick={() => setTab('history')}>
                    Zakończone rezerwacje ({endedReservations.length})
                </button>
            </div>

            <table>
                <thead>
                <tr>
                    <th>Boisko</th>
                    <th>Data rozpoczęcia rezerwacji</th>
                    <th>Data zakończenia rezerwacji</th>
                    <th>Cena</th>
                </tr>
                </thead>
                <tbody>
                {displayedReservations.length > 0 ? (
                    displayedReservations.map((res: any) => (
                        <tr key={res.id}>
                            <td>{getRoomName(res.roomId)}</td>

                            <td>{formatDate(res.startTime)}</td>
                            <td>
                                {res.endTime ? formatDate(res.endTime) :
                                    <span style={{color: '#4caf50'}}>W TRAKCIE</span>}
                            </td>
                            <td>{res.price || '-'} PLN</td>
                        </tr>
                    ))
                ) : (
                    <tr key={"null"}>
                        <td colSpan={4}>
                            Brak dostępnych rezerwacji
                        </td>
                    </tr>
                )}
                </tbody>
            </table>

            <button className="btn" onClick={() => navigate('/users')}>
                Powrót
            </button>
        </div>
    );
}