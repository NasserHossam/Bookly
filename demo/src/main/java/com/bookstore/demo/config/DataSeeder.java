package com.bookstore.demo.config;

import com.bookstore.demo.entity.Book;
import com.bookstore.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

// Seeds the catalog with real, well-known books (real titles/authors/ISBN-based
// cover images) the first time the app runs against an empty `books` table.
// Runs once: if the table already has rows (e.g. an admin added books, or this
// already ran before) it does nothing, so it never overwrites your data.
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final BookRepository bookRepository;

    private static final String COVER_BASE = "https://covers.openlibrary.org/b/isbn/";

    @Override
    public void run(String... args) {

        if (bookRepository.count() > 0) {
            return;
        }

        bookRepository.saveAll(List.of(

                // --- Fiction ---
                book("To Kill a Mockingbird", "Harper Lee", "Fiction", 12.99,
                        "A young girl in Depression-era Alabama watches her father, a principled lawyer, defend a Black man falsely accused of a crime. A quiet, devastating look at justice, prejudice, and growing up.",
                        "9780061120084"),

                book("The Great Gatsby", "F. Scott Fitzgerald", "Fiction", 10.99,
                        "Jazz-Age New York, a mysterious millionaire, and the woman he can't let go of. A short, glittering novel about ambition, class, and the cost of chasing a dream that was never really yours to have.",
                        "9780743273565"),

                book("The Catcher in the Rye", "J.D. Salinger", "Fiction", 11.99,
                        "Sixteen-year-old Holden Caulfield wanders New York City after being expelled from school, restless and cynical about the adult world closing in on him. A landmark voice in coming-of-age fiction.",
                        "9780316769488"),

                // --- Fantasy ---
                book("The Hobbit", "J.R.R. Tolkien", "Fantasy", 14.99,
                        "Bilbo Baggins is dragged out of his comfortable hobbit-hole on a quest to reclaim a dwarf kingdom from a dragon. The warm, adventurous prelude to The Lord of the Rings.",
                        "9780547928227"),

                book("Harry Potter and the Sorcerer's Stone", "J.K. Rowling", "Fantasy", 13.99,
                        "An orphaned boy discovers on his eleventh birthday that he's a wizard, and is whisked off to Hogwarts School of Witchcraft and Wizardry. The book that launched a generation of readers.",
                        "9780590353427"),

                book("The Name of the Wind", "Patrick Rothfuss", "Fantasy", 16.99,
                        "An innkeeper with a hidden past recounts, in his own words, how he became the most notorious wizard, thief, and musician his world has ever known. Dense, lyrical modern fantasy.",
                        "9780756404079"),

                // --- Science Fiction ---
                book("Dune", "Frank Herbert", "Science Fiction", 15.99,
                        "On the desert planet Arrakis, a young heir becomes entangled in a struggle over the galaxy's most valuable resource. Sweeping political and ecological science fiction on an epic scale.",
                        "9780441172719"),

                book("Brave New World", "Aldous Huxley", "Science Fiction", 12.99,
                        "A genetically engineered, pleasure-drugged future society looks perfect on the surface — until one man starts asking what's been given up to build it. A chilling counterpart to Orwell's 1984.",
                        "9780060850524"),

                book("Ender's Game", "Orson Scott Card", "Science Fiction", 13.99,
                        "A gifted child is trained at a military academy in orbit to command Earth's fleet against an alien threat, in a story that turns into an unflinching look at what war does to children.",
                        "9780812550702"),

                // --- Mystery & Thriller ---
                book("Gone Girl", "Gillian Flynn", "Mystery & Thriller", 14.99,
                        "On his fifth wedding anniversary, Nick's wife vanishes and the police start to suspect him. A twisting psychological thriller about a marriage built on lies.",
                        "9780307588371"),

                book("The Silent Patient", "Alex Michaelides", "Mystery & Thriller", 13.99,
                        "A woman shoots her husband and never speaks another word. A criminal psychotherapist becomes obsessed with uncovering why. A tightly plotted thriller with a twist readers rarely see coming.",
                        "9781250301697"),

                book("The Da Vinci Code", "Dan Brown", "Mystery & Thriller", 12.99,
                        "A murder in the Louvre leads a symbologist and a cryptologist down a trail of hidden codes, secret societies, and centuries-old conspiracies. Fast-paced puzzle-box fiction.",
                        "9780307474278"),

                // --- Romance ---
                book("Pride and Prejudice", "Jane Austen", "Romance", 9.99,
                        "Elizabeth Bennet and the proud Mr. Darcy clash their way toward one of literature's most beloved love stories, wrapped in Austen's sharp social wit.",
                        "9781503290563"),

                book("The Fault in Our Stars", "John Green", "Romance", 10.99,
                        "Two teenagers who meet at a cancer support group fall in love, fully aware of how little time they may have. Funny, aching, and unsentimental about illness and love alike.",
                        "9780142424179"),

                book("Me Before You", "Jojo Moyes", "Romance", 12.99,
                        "A free-spirited young woman takes a job caring for a man paralyzed after an accident, and neither of their lives goes the way either of them expected. A tender, heart-wrenching romance.",
                        "9780143124542"),

                // --- Self-Help ---
                book("Atomic Habits", "James Clear", "Self-Help", 16.99,
                        "A practical guide to building good habits and breaking bad ones through small, compounding changes. One of the most widely recommended habit-building books in print.",
                        "9780735211292"),

                book("The Subtle Art of Not Giving a F*ck", "Mark Manson", "Self-Help", 14.99,
                        "A counterintuitive take on self-help that argues a happier life comes from caring about fewer, better things — not from relentless positivity.",
                        "9780062457714"),

                book("Think and Grow Rich", "Napoleon Hill", "Self-Help", 9.99,
                        "A classic personal-development text distilling the habits and mindset Hill observed in successful people of his era. Still widely read nearly a century later.",
                        "9781585424337"),

                // --- Business ---
                book("Rich Dad Poor Dad", "Robert Kiyosaki", "Business", 11.99,
                        "Kiyosaki contrasts the financial lessons of his own father with those of a friend's wealthy father, making the case for financial literacy and building assets over trading time for money.",
                        "9781612680194"),

                book("Zero to One", "Peter Thiel", "Business", 15.99,
                        "PayPal co-founder Peter Thiel argues that real progress comes from creating something new, not competing over what already exists. A contrarian look at startups and innovation.",
                        "9780804139298"),

                book("The Lean Startup", "Eric Ries", "Business", 14.99,
                        "A methodology for building companies through rapid experimentation, validated learning, and iterative product releases instead of long, unvalidated planning cycles.",
                        "9780307887894"),

                // --- History & Non-Fiction ---
                book("Sapiens: A Brief History of Humankind", "Yuval Noah Harari", "History & Non-Fiction", 17.99,
                        "A sweeping account of how Homo sapiens came to dominate the planet, tracing the cognitive, agricultural, and scientific revolutions that shaped human history.",
                        "9780062316097"),

                book("Educated", "Tara Westover", "History & Non-Fiction", 14.99,
                        "Raised by survivalist parents in rural Idaho with no formal schooling, Westover eventually makes her way to a classroom for the first time at seventeen — and, years later, to a PhD. A memoir about the cost of self-invention.",
                        "9780399590504"),

                book("A Short History of Nearly Everything", "Bill Bryson", "History & Non-Fiction", 15.99,
                        "Bryson takes on the history of science itself — from the Big Bang to the rise of civilization — translating it for readers with no scientific background, with humor throughout.",
                        "9780767908184")
        ));
    }

    private Book book(String title, String author, String category, double price,
                       String description, String isbn13) {

        return Book.builder()
                .title(title)
                .author(author)
                .category(category)
                .price(price)
                .description(description)
                .imageUrl(COVER_BASE + isbn13 + "-L.jpg")
                .build();
    }
}
