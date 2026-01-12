import {useEffect, useState} from 'react';
import {useForm} from 'react-hook-form';
import axiosSetup from '../api/axiosSetup.ts';
import {useNavigate} from 'react-router-dom';
import {toast} from 'react-toastify';

export default function ReservationForm() {
    const navigate = useNavigate();
    const {register, handleSubmit} = useForm();

    const [clients, setClients] = useState<any[]>([]);
    const [rooms, setRooms] = useState<any[]>([]);

    useEffect(() => {
        axiosSetup.get('/users')
            .then(res => setClients(res.data))
            .catch(() => toast.error("Błąd podczas pobierania użytkowników"));

        axiosSetup.get('/rooms')
            .then(res => setRooms(res.data))
            .catch(() => toast.error("Błąd podczas pobierania boisk"));
    }, []);

    const onSubmit = async (data: any) => {
        if (!window.confirm("Czy potwierdzasz rezerwację?")) return;

        try {
            await axiosSetup.post('/reservations', data);

            toast.success("Rezerwacja dodana");
            navigate('/reservations');
        } catch (e: any) {
            console.error(e);
            const msg = e.response?.data?.message || "Wystąpił błąd podczas rezerwacji boiska";
            toast.error(msg);
        }
    };

    return (
        <div className="container">
            <form onSubmit={handleSubmit(onSubmit)} className="form-container">
                <h2>Stwórz nową rezerwację</h2>
                <div className="form-group">
                    <label>Wybierz klienta</label>
                    <select {...register('clientId', {required: true})} className="form-control">
                        <option value="">-- Wybierz Klienta --</option>
                        {clients.map((c: any) => (
                            <option key={c.id || c.userId} value={c.id || c.userId}>
                                {c.firstName} {c.lastName} ({c.login})
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>Wybierz boisko sportowe</label>
                    <select {...register('roomId', {required: true})} className="form-control">
                        <option value="">-- Wybierz Boisko --</option>
                        {rooms.map((r: any) => (
                            <option key={r.id} value={r.id}>
                                {r.roomType} (Cena bazowa: {r.basePrice} PLN)
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>Start rezerwacji od</label>
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