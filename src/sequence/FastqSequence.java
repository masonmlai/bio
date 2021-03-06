package sequence;

public final class FastqSequence extends FastaSequence {

    private final byte[] quality;
    
    public FastqSequence(String name, String seq, byte[] quality) {
        super(name, seq);
        this.quality = quality.clone();
    }
    
    public FastqSequence(String name, String seq, String quality,
            PhredEncoding pe) {
        super(name, seq);
        
        if (quality == null) {
            throw new IllegalArgumentException("INSTANTIATION FAIL: " +
                    "Attempted to create FastqSequence with null quality " + 
                    "String");
        }
        
        if (pe == null) {
            throw new IllegalArgumentException("INSTANTIATION FAIL: " +
                    "Attempted to create FastqSequence with null " + 
                    "PhredEncoding");
            
        }
        
        if (seq.length() != quality.length()) {
            throw new IllegalArgumentException("INSTANTIATION FAIL: " +
                    "Sequence bases " + seq + " and sequence quality scores " +
                    quality + " do not agree.");
        }
        
        this.quality = pe.stringToPhred(quality);
    }
    
    public FastqSequence(String name, String seq, String quality) {
        this(name, seq, quality, PhredEncoding.SANGER);
    }
    
    @Override
    public FastqSequence changeName(String name) {
        return new FastqSequence(name, sequence, quality);
    }
    
    @Override
    public FastqSequence reverseComplement() {
        return reverseComplement(name);
    }
    
    @Override
    public FastqSequence reverseComplement(String name) {
        byte[] reverseQuality = new byte[quality.length];
        for (int i = 0; i < quality.length; i++) {
            reverseQuality[i] = quality[quality.length - i - 1];
        }
        
        return new FastqSequence(name, Sequences.reverseComplement(sequence),
                reverseQuality);
    }
    
    @Override
    public FastqSequence subsequence(int start, int end) {
        return subsequence(name, start, end);
    }
    
    @Override
    public FastqSequence subsequence(String name, int start, int end) {
        String subseq = sequence.substring(start, end);
        byte[] subqual = new byte[end - start];
        for (int i = start; i < end; i++) {
            subqual[i - start] = quality[i];
        }
        return new FastqSequence(name, subseq, subqual);
    }
    
    @Override
    public String toFormattedString() {
        return "@" + name + nl + sequence + nl + "+" + nl + quality;
    }
    
    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        }
        
        if (!(other instanceof FastqSequence)) {
            return false;
        }

        FastqSequence o = (FastqSequence) other;
        
        return quality.equals(o.quality) && name.equals(o.name)
                && sequence.equals(o.sequence);
    }
    
    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode = 37 * hashCode + name.hashCode();
        hashCode = 37 * hashCode + sequence.hashCode();
        hashCode = 37 * hashCode + quality.hashCode();
        return hashCode;
    }
}