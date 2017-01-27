package com.contentful.hello;

import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAResource;
import com.contentful.java.cda.CDASpace;

/**
 * Main class of a simple example using Contentful.
 * <p>
 * This sample code shows you how to do the bare minimum of things needed to connect your java app
 * with Contentful.
 * It'll create a client to communicate with Contentful, fetches information about the space it is
 * connected to, and shows how to retrieve all entries of this space and lastly how to filter
 * search your space for specific criteria.
 * </p>
 * <h1>Compilation</h1>
 * {@code ./gradlew build}
 * <h1>Running</h1>
 * {@code ./gradlew run}
 *
 * @see <a href="http://contentful.com/">Contentful Main Website</a>
 * @see <a href="http://app.contentful.com/">Contentful Management WebApp</a>
 * @see <a href="http://doc.contentful.com/java/hello-world">Contentful documentation</a>
 */
public class Main {

  /**
   * Token to communicate with the <b>c</b>ontent <b>d</b>elivery <b>a</b>pi.
   * Please use the WebApp to find your token, if it is not present here.
   *
   * @see <a href="http://app.contentful.com">WebApp</a>
   */
  private static final String CDA_TOKEN = "297e67b247c1a77c1a23bb33bf4c32b81500519edd767a8384a4b8f8803fb971";

  /**
   * Space <b>id</b>entifier.
   * <p>
   * This identifier is used to know which space you want to request items from. Please also use the
   * WebApp, if this value is not set.
   *
   * @see <a href="http://app.contentful.com">WebApp</a>
   */
  private static final String SPACE_ID = "71rop70dkqaj";


  /**
   * Main method of the app
   * <p>
   * This method is the entry point of your app. All important communications with Contentful will
   * be happening here.
   *
   * @param args arguments passed at runtime, not used by this sample.
   */
  public static void main(String[] args) {

    /*
     * Create the client
     *
     * This client will abstract the communication to Contentful. Use it to make your requests to
     * Contentful.
     *
     * For initialization it needs the {@link #CDA_TOKEN} and {@link #SPACE_ID} from above.
     */
    final CDAClient client = CDAClient
        .builder()
        .setToken(CDA_TOKEN)
        .setSpace(SPACE_ID)
        .build();

    /*
     * After you created your client, let us retrieve the space we'll be using in this sample. The
     * following request will be done synchronously, so the thread will be blocked till Contentful
     * gives us an answer.
     */
    final CDASpace space = client.fetchSpace();

    /*
     * Now that we have a space, we can find out the name of it. Thankfully the Contentful SDK has
     * already created an object based on the response from the Contentful API: A {@see CDASpace}.
     * This object does not contain all the entries of this space, it just allows us to retrieve the
     * general information of the space. Let us print the name we set in the WebApp of this space to
     * the command line.
     */
    System.out.println("Fetched space with name: " + space.name());

    /*
     * Since we are actually more interested in the contents of the space, let us fetch all entries
     * of this space. The following statement will fetch all {@code Entries} of the space. We could
     * also fetch all {@see CDAAssets} and all {@see CDAContentTypes} of this space, by simply
     * exchanging the given CDAEntry class with the wanted one.
     */
    final CDAArray entries = client.fetch(CDAEntry.class).all();

    /*
     * The following snipped will print out the id and type of all entries requested.
     */
    for (final CDAResource resource : entries.items()) {
      // We are sure that all resources returned are a CDAEntry, since we specified it in the fetch
      // so we can directly cast it to an entry.
      final CDAEntry entry = (CDAEntry) resource;
      System.out.println(String.format("%s: %s", entry.id(), entry.contentType()));
    }
    System.out.println();

    /*
     * The last thing we want to show with this app, is how to filter all of entries in a space, so
     * that we only return entries of a given type. Therefore we'll just get the type of the first
     * entry we requested above, and ask Contentful to return us all entries of this type,
     * printing the first field of it.
     */
    final String contentTypeId = (String) ((CDAEntry) entries.items().get(0)).contentType()
        .attrs()
        .get("id");

    final CDAArray entriesOfSameType = client
        .fetch(CDAEntry.class)
        .where("content_type", contentTypeId)
        .all();

    for (final CDAResource resource : entriesOfSameType.items()) {
      final CDAEntry entry = (CDAEntry) resource;
      System.out.println(String.format("%s of %s", entry.id(), entry.getAttribute("contentType")));
    }

    /*
     * And we are done. Please feel free to stick around, change some code to see how it works, or
     * continue reading more in depth guides at {@see <a href="docs.contentful.com/java"> our
     * documentation</a>}
     */
  }
}
