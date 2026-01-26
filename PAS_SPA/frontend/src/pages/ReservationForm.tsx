import {useEffect, useState} from 'react';
import {useForm} from 'react-hook-form';
import {useNavigate} from 'react-router-dom';
import {toast} from 'react-toastify';
import axiosSetup from '../api/axiosSetup';
import {useAuth} from '../context/LoggedUserContext';
import {RoleEnum} from '../HandleProtection';

export default function ReservationForm() {
    const navigate = useNavigate();
    const {userRole, userLogin} = useAuth();

    const {register, handleSubmit, setValue} = useForm();

    const [clients, setClients] = useState<any[]>([]);
    const [rooms, setRooms] = useState<any[]>([]);
    const [myId, setMyId] = useState<string>('');

    useEffect(() => {
        axiosSetup.get('/rooms')
            .then(res => setRooms(res.data))
            .catch(() => toast.error("Błąd podczas pobierania boisk"));

        axiosSetup.get('/users')
            .then(res => {
                const data = Array.isArray(res.data) ? res.data : [res.data];

                const onlyClients = data.filter((u: any) => u.role === 'CLIENT');

                setClients(onlyClients);

                if (userRole === RoleEnum.CLIENT) {
                    const me = data.find((u: any) => u.login === userLogin);
                    if (me) {
                        const id = me.id || me.userId;
                        setMyId(id);
                        setValue('clientId', id);
                    }
                }
            })
            .catch(() => toast.error("Błąd podczas pobierania użytkowników"));
    }, [userRole, userLogin, setValue]);

    const onSubmit = async (data: any) => {
        if (!window.confirm("Czy potwierdzasz rezerwację?")) return;

        try {
            let safeStartTime = data.startTime;
            if (safeStartTime && safeStartTime.length === 16) {
                safeStartTime += ':00';
            }
            const payload = {
                roomId: data.roomId,
                clientId: userRole === RoleEnum.CLIENT ? myId : data.clientId,
                startTime: safeStartTime,
                price: 0.0
            };

            await axiosSetup.post('/reservations', payload);

            toast.success("Rezerwacja dodana");
            navigate('/reservations');
        } catch (e: any) {
            console.error(e);
            const backendMsg = e.response?.data?.message || e.response?.data?.error;
            toast.error(backendMsg || "Nie udało się utworzyć rezerwacji");
        }
    };

    return (
        <div className="container">
            <form onSubmit={handleSubmit(onSubmit)} className="form-container">
                <h2>Stwórz nową rezerwację</h2>

                <div className="form-group">
                    <label>Klient</label>

                    {userRole === RoleEnum.ADMIN ? (
                        <select
                            {...register('clientId', {required: true})}
                            className="form-control"
                        >
                            <option value="">-- Wybierz Klienta --</option>
                            {clients.map((c: any) => (
                                <option key={c.id} value={c.id}>
                                    {c.firstName} {c.lastName} ({c.login})
                                </option>
                            ))}
                        </select>
                    ) : (
                        <input
                            type="text"
                            value={userLogin}
                            disabled
                            className="form-control"
                        />
                    )}
                </div>

                <div className="form-group">
                    <label>Wybierz boisko sportowe</label>
                    <select
                        {...register('roomId', {required: true})}
                        className="form-control"
                    >
                        <option value="">-- Wybierz Boisko --</option>
                        {rooms.map((r: any) => {
                            const price = r.basePrice;
                            const name = r.roomType;
                            const id = r.id;
                            return (
                                <option key={id} value={id}>
                                    {name} (Cena: {price} PLN)
                                </option>
                            );
                        })}
                    </select>
                </div>

                <div className="form-group">
                    <label>Start rezerwacji</label>
                    <input
                        type="datetime-local"
                        className="form-control"
                        {...register('startTime', {required: true})}
                    />
                </div>

                <button type="submit" className="btn">
                    Zarezerwuj
                </button>
            </form>
        </div>
    );
}